var http    = require("http");
var url     = require("url");
var fs      = require("fs");
var path    = require("path");
var port    = process.env.PORT || 3000;
var dataGen = require('./EuHierarchyData');

var server  = http.createServer(function(request, response) {

  var uri        = url.parse(request.url).pathname
  
  if(uri == '/jquery/jquery.scrollTo-1.4.3.1.js'){
	  uri = '../../..' + uri
  }
  if(uri == '/jquery/jquery-1.8.1.js'){
	  uri = '../../..' + uri
  }
  if(uri == '/images/spinner.gif'){
	  uri = '../../../..' + uri
  }
  var filename   = path.join(process.cwd(), uri);
  var serverName = request.headers.host;
  var params     = url.parse(request.url, true).query; 
  
  if(params.requestData){
	  var data = eval(  params.requestData  );
	  console.log('data = ' + JSON.stringify(data));	  
	  response.writeHead(200, {"Content-Type": "application/json"});
	  response.write(  JSON.stringify(data)  );
	  response.end();
  }
  else{
	  
    filename = path.resolve(filename);
	
    fs.readFile(filename, "binary", function(err, file) {
        if(err) {        
          response.writeHead(500, {"Content-Type": "text/plain"});
          response.write('Error reading file ' + filename + ':\n\nserverName: ' + serverName + ', uri: ' + uri + ', params: ' + JSON.stringify(params));
          response.write(err + "\n");
          response.end();
          return;
        }
        
        response.write(file, "binary");
        response.end();
    });
	  
  }  
      
    
}).listen(port);

console.log('Server running on port ' + server.address().port);

  