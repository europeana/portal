css_dir = "../default/css"
sass_dir = "src"


module Sass::Script::Functions
  def web_server()
    #Sass::Script::String.new(Compass.configuration.webserver) 
  #end
  
    if(defined?(Compass.configuration.webserver))
       Sass::Script::String.new(Compass.configuration.webserver) 
    else
       Sass::Script::String.new("http://localhost:8080/portal/") 
    end
  end
end

#def web_server()
#  Compass.configuration.webserver  == nil ? "http://localhost:8080/portal/" : Compass.configuration.webserver 
#end

