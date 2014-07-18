var http    = require("http");
var url     = require("url");
var fs      = require("fs");
var path    = require("path");
var port    = process.env.PORT || 3000;


var data = {"data":[]};
var max  = 10;
var wrapTitle = function(title){
	return  {
            "def": [ title ]
	}
}

/*
BOOKS
	VOLUMES
		CHAPTERS
			VERSES
*/			

for(var i=0; i<4; i++){
	
	data.data[data.data.length] = { "id" : i+"", "index":i, "title" : wrapTitle("Book " + i), "type" : "text", "data" : [] };
		
	for(var j=0; j<4; j++){

		var book = data.data[data.data.length-1];
		book.data[book.data.length] = { "id" : i+"-"+j, "index":j, "title" : wrapTitle("Volume " + j + ' (b' + i + ')'), "type" : "text", "data" : [] };
		
		for(var k=0; k<4; k++){
			
			var volume = book.data[book.data.length-1];
			volume.data[volume.data.length] = { "id" : i+"-"+j+"-"+k, "index":k, "title" : wrapTitle("Chapter " + k + ' (b' + i + ', v' + j + ')'), "type" : "text", "data" : [] };

			for(var l=0; l<4; l++){
				
				var chapter = volume.data[volume.data.length-1];
				chapter.data[chapter.data.length] = { "id" : i+"-"+j+"-"+k+"-"+l, "index": l,  "title" : wrapTitle("Verse " + l + ' (b' + i + ', v' + j + ', c' + k + ')'), "type" : "text" };

			}
		}
	}
}

// utility for result write
var parentData = function(path, limit){
	return {
        "id": path.slice(0, path.length-1).join('-'),
        "title": search(path.slice(0, path.length-1).join('-'), 'self.json', limit).object.title,
        "type": "TEXT",
        "index": path[path.length-2],
        "hasChildren": true
	}
}

// utility for result write
var coreData = function(data){
	if(!data){
		return;
	}
	return {
		"id" :			data.id,
		"title" :		data.title,
        "type":			data.type,
        "index":		data.index,
        "hasChildren":	typeof data.data==='object'					
	}
}

// search
var search = function(id, action, limit){
	
	console.log('search on id: ' + id + ' with action: ' + action + ' and limit: ' + limit + ' typeof ' + typeof limit);
	
	var res   = { "action" : action };
	var path  = id.split('-');
	var sData = JSON.parse(JSON.stringify(data));

	if(!sData){
		return false;
	}
	
	for(var i=0; i<path.length; i++){
		sData = sData.data[parseInt(path[i])];
	}

	/*
		SELF
	*/
	if(action==="self.json"){
		res.object    =  coreData(sData);
		
		if(res.object){
			res.hasParent = path.length>1;
			
			if(res.hasParent){
				res.parent = parentData(path, limit);
			}
			if(res.object.hasChildren){
				res.childrenCount = sData.data.length;
			}			
		}
	}
	
	/*
		CHILDREN
	*/

	else if(action==="children.json"){
		res.children = [];
		for(var i=0; i< Math.min(limit, sData.data.length); i++){
			res.children.push(coreData(sData.data[i]))
		}
		res.hasParent = path.length>1;
		res.childrenCount = sData.data.length;
	}
	
	/*
		FOLLOWING
	*/

	else if(action==="following-siblings.json"){
		
		res.hasParent = false;
		res['following-siblings'] = [];
		
		if(path.length>1){

			res.parent = parentData(path, limit);
			
			var parentPath		= path.slice(0, path.length-1).join('-');
			var parentChildren	= search(path.slice(0, path.length-1).join('-'), 'children.json', max);					
			var start			= false;
			var added			= 0;
			
			for(var i=0; i<parentChildren.children.length; i++){
				if(start){
					res['following-siblings'].push(coreData(parentChildren.children[i]));
					added ++;
					if(added==limit){
						break;
					}
				}
				if(!start && parentChildren.children[i].id == id){
					start = true;
				}
			}			
			res.hasParent = true;
		}
		if(sData.data){
			res.childrenCount = sData.data.length;					
		}
		
	}
	
	/*
		PRECEDING
	*/

	else if(action==="preceeding-siblings.json"){

		res['preceeding-siblings'] = [];
		res.hasParent = false;
		
		if(path.length>1){
			
			res.parent = parentData(path, limit);
			
			var parentChildren	= search(path.slice(0, path.length-1).join('-'), 'children.json', max);
			var start 			= false;
			var added			= 0;
			
			for(var i=parentChildren.children.length-1; i>=0; i--){
				
				if(start){
					res['preceeding-siblings'].push(coreData(parentChildren.children[i]));
					added ++;
					if(added==limit){
						break;
					}
				}
				if(!start && parentChildren.children[i].id == id){
					start = true;
				}
			}

			res.hasParent = true;
		}
		if(sData.data){
			res.childrenCount = sData.data.length;					
		}
	}
	return res;
}


var server  = http.createServer(function(request, response) {

  var uri        = url.parse(request.url).pathname
  var fileName   = path.join(process.cwd(), uri);
  var params     = url.parse(request.url, true).query; 

  console.log('uri ' + uri)
  console.log('fileName ' + fileName)
  console.log('params ' + JSON.stringify(params))
  
  fileName = fileName.split('/');
  
  var action = fileName.pop();
  var record = fileName.pop();
  var limit  = params.limit;
  
  console.log('action ' + action)
  console.log('record ' + record)
  
  var result = search(record, action, limit);
  result.success = true;
  
  // CORS compliance
  response.setHeader('Access-Control-Allow-Origin', '*');
  response.setHeader('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE,OPTIONS');
  response.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization, Content-Length, X-Requested-With');
  
  response.writeHead(200, {"Content-Type": "application/json"});
  response.write(  JSON.stringify(result)  );
  response.end();
    
}).listen(port);

console.log('Server running on port ' + server.address().port);

  