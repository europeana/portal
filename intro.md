Introduction for portal developers
==

Controllers and parameters
---
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

Models
---
The models are Java classes to store values which finally the JSP pages will print out to the user. In the portal the model classes has a big hierarchy, where the root object is always the abstract ``eu.europeana.corelib.web.model.PageData`` in the corelib. Some model class are abstracts, usually the contain some variables plus accessor methods, but sometimes they has heavy business logic, mainly bound to the logic of view. Sometimes the chain of extension is really long, for example: ``SearchPage`` -> ``SearchPreparation`` -> ``SearchData`` -> ``UrlAwareData`` -> ``ResultPageData`` -> ``SearchPageData`` -> ``PortalPageData`` -> ``PageData``.

Views
---
The views of the portal are registered in ``eu.europeana.portal2.web.presentation.PortalPageInfo`` enumeration. A definition looks like:

    SEARCH_HTML("search.html", "Europeana - Search results", "search/search")

The real important part is the third argument here, which is the path of the template. It will be resolved as ``src/main/webapp/WEB-INF/jsp/[theme]/[template].jsp``, where ``theme`` will be resolved as „default” (since we only have this theme right now), so you will find the jsp page as ``src/main/webapp/WEB-INF/jsp/default/search/search.jsp`` (for the configuration see ``viewResolver`` bean in ``portal2-mvc.xml`` Spring configuration file).

Configuration
---
The portal and API uses the europeana.properties file to store configuration keys. The is a class in corelib  eu.europeana.corelib.web.support.Configuration, which reads these keys, and provides getters to access the values. This class is registered as Spring bean, so in a controller or in other beans you can reference it as a resource, such as

    @Resource
    private Configuration config;

If you want to add new property, please adjust the Configuration class. The ``@Value`` annotation and the structure of property file let us to store simple key-value entries, but we did some tricks to add string lists, and maps.

### A map example

In property file:

    portal.seeAlso.field.1 = title=see_also_title_t
    portal.seeAlso.field.2 = who=see_also_who_t
    portal.seeAlso.field.3 = what=see_also_what_t
    portal.seeAlso.field.4 = PROVIDER=see_also_provider_t

In Configuration:

    public Map<String, String> getSeeAlsoTranslations() {
      if (seeAlsoTranslations == null) {
        seeAlsoTranslations = new HashMap<String, String>();
        int i = 1;
        while (europeanaProperties.containsKey("portal.seeAlso.field." + i)) {
          String[] parts = europeanaProperties.getProperty("portal.seeAlso.field." + i).split("=", 2);
          seeAlsoTranslations.put(parts[0].trim(), parts[1].trim());
          i++;
        }
      }
      return seeAlsoTranslations;
    }

### A list example

In property file:

    portal.soundCloudAwareCollections = x,y,z

In Configuration:

    @Value("#{europeanaProperties['portal.soundCloudAwareCollections']}")
    private String soundCloudAwareCollectionsString;
    
    ...
    public List<String> getSoundCloudAwareCollections() {
      if (soundCloudAwareCollections == null) {
        String[] items = soundCloudAwareCollectionsString.split(",");
        soundCloudAwareCollections = new ArrayList<String>();
        for (String item : items) {
          soundCloudAwareCollections.add(item.trim());
        }
      }
      return soundCloudAwareCollections;
    }


Services
---
The corelib provides a number of services you need to access resources like the Solr, Mongo, or similar things. If you want to add new service which possibly will be used in both portal and API, please add it to the corelib. There are some services, which used only by the portal, they exist in eu.europeana.portal2.services package in subpackages. The service should define in an interface, in order to be possible to create concrete implementation, and mock implementation which will be used in unit or user interface tests. The service should be registered as bean in a Spring config file, and can be used in controllers and in other beans with the ``@Resource`` annotation.

portal2-services.xml:

    <bean name="portal2_clickStreamLogService"
          class="eu.europeana.portal2.services.impl.ClickStreamJsonLogServiceImpl"/>


SearchController:

    @Resource
    private ClickStreamLogService clickStreamLogger;

