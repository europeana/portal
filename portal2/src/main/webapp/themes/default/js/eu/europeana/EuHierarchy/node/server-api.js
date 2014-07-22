var http    = require("http");
var url     = require("url");
var fs      = require("fs");
var path    = require("path");
var port    = process.env.PORT || 3000;
var dataGen = require('./EuHierarchyData2');


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
  var max    = 10;
  var limit  = params.limit ? params.limit : max;
  
  console.log('action ' + action)
  console.log('record ' + record)
  console.log('action ' + typeof action + ' - ' + action==null);
  
  if(!action || action=='index.html'){
	    fs.readFile('index.html', "binary", function(err, file) {
	        if(err) {        
	          response.writeHead(500, {"Content-Type": "text/plain"});
	          response.write('Error reading file ' + fileName + ':\n\nserverName: ' + serverName + ', uri: ' + uri + ', params: ' + JSON.stringify(params));
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

  