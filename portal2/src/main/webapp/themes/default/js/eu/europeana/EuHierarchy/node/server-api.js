var http    = require("http");
var url     = require("url");
var fs      = require("fs");
var path    = require("path");
var port    = process.env.PORT || 3000;
var dataGen = require('./EuHierarchyData2');


var server  = http.createServer(function(request, response) {

  var uri        = url.parse(request.url).pathname
  var fileName;
  var sFileName;
  var params;
  
  if(uri.indexOf('.css')==(uri.length-4)){
	  uri = '..' + uri
  }
  else if(uri.indexOf('.gif')==(uri.length-4)){
	  uri = '..' + uri
  }
  else if(uri.indexOf('.png')==(uri.length-4)){
	  uri = '..' + uri
  }
  else if(uri.indexOf('jquery') > -1 ){
	  uri = '../../../..' + uri
  }
  else if(uri.indexOf('EuHierarchyTools') > -1 ){
	  
  }
  else if(uri.indexOf('.js')==(uri.length-3)){
	  uri = '..' + uri
  }
  
  fileName   = path.join(process.cwd(), uri);
  params     = url.parse(request.url, true).query; 
  
  
  console.log('uri ' + uri)
  console.log('fileName ' + fileName)
  console.log('params ' + JSON.stringify(params))
  
  sFileName = fileName;
  fileName = fileName.split('/');
  
  var action = fileName.pop();
  var record = fileName.pop();
  var max    = 10;
  var limit  = params.limit ? params.limit : max;
  
  console.log('action ' + action)
  console.log('record ' + record)
  console.log('action ' + typeof action + ' - ' + action==null);
 
  var serveFile = function(action){
	  var res = false;
	  var extensions = ['.html', '.css', '.js', '.png', '.gif'];
	  for(var i=0; i<extensions.length; i++){
		  if( action.indexOf(extensions[i])==(action.length-extensions[i].length) ){
			  res = true;
		  }		  
	  }
	  return res;
  }
  
  if(!action || serveFile(action)){	  	  
	    fs.readFile(sFileName, "binary", function(err, file) {
	        if(err) {        
	          response.writeHead(500, {"Content-Type": "text/plain"});
	          response.write('Error reading file "' + sFileName + '":<br/>, uri: ' + uri + ', params: ' + JSON.stringify(params));
	          response.write(err + "\n");
	          response.end();
	          return;
	        }
	        response.write(file, "binary");
	        response.end();
	    });
	    return;
  }
  
  var result     = dataGen.search(record, action, limit);
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

  