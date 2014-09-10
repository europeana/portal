Introduction for portal developers
===

The portal entry points are the controllers, they take place in eu.europeana.portal2.web.controllers package and its subpackages. A controller class is annotated with the ```@Controller``` annotation. The URLs and controller methods are mapped with the ```@RequestMapping``` annotation, where the ```value``` parameter means the URL, relative to the application's root.

For example

    @Controller
    public class ApiConsoleController {
      ...
  	  @RequestMapping(value = "/api/console.html", produces = MediaType.TEXT_HTML_VALUE)
      public ModelAndView console(...) {...}
      ...  
    }

means, that when the user enters the http://europeana.eu/portal/api/console.html URL, the application will trigger the console() method of the ApiConsoleController. The controllers are singletons, so the have only one instances, and they are instanciated when application starts. Spring reads the annotations, and does the mapping, and reports it to the catalina.out log:

     Sep 09, 2014 11:25:06 AM org.springframework.web.servlet.handler.AbstractHandlerMethodMapping registerHandlerMethod
     INFO: Mapped "{[/api/console.html],methods=[],params=[],headers=[],consumes=[],produces=[text/html],custom=[]}" onto public org.springframework.web.servlet.ModelAndView eu.europeana.portal2.web.controllers.ApiConsoleController.console(java.lang.String,java.lang.String,java.lang.String,java.lang.String[],java.lang.String[],int,int,java.lang.String,java.lang.String,java.lang.String,boolean,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String[],java.lang.String,javax.servlet.http.HttpServletRequest,java.util.Locale)

Spring pass the HttpServletRequest, HttpServletResponse and Locale parameters if you add it to the method signature. Europeana portal and API uses two types of parameters in the URL. One is called path variable, these are values take place in the path of the URL. In the URL http://europeana.eu/portal/record/2032009/Hungarian_National_Museum_data_hnm_hu_object_1195535.html the „2032009” and „Hungarian_National_Museum_data_hnm_hu_object_1195535” are path variables, and Spring resolve them with the ```@PathVariable``` annotation.


    @RequestMapping(value = "/record/{collectionId}/{recordId}.html", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView record(
			@PathVariable String collectionId, 
			@PathVariable String recordId,
			...) {...}

The other kind of parameter is the normal query parameter, which is denoted by the ```@RequestParam``` annotation. In URL http://europeana.eu/portal/search.html?query=Paris&rows=24 „query” and „rows” are such parameters, here is how you can extract them:

    @RequestMapping({"/search.html", "/brief-doc.html"})
    public ModelAndView searchHtml(
			@RequestParam(value = "query", required = false, defaultValue = "*:*") String queryString,
			@RequestParam(value = "rows", required = false, defaultValue = "24") int rows,
			...) {...}

In this example the ```@RequestMapping``` has multiple mapping, so the same resource is accessible by two URLs. The ```@RequestParam``` has multiple argument, the ``value`` denotes the URL parameter name, ``required`` denotes if it is a required paramter, and if user does not provide it, Spring will return a ``HTTP/1.1 400 Bad Request`` error, stating that „The request sent by the client was syntactically incorrect.” ``defaultValue`` lets you set a default value (which will be overriden by the user entered value. At the end of annotation ``String queryString`` maps the parameter to a method argument, so in the body of the method you access it via ``queryString``.

