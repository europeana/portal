package eu.europeana.portal2.web.presentation;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

import org.jruby.embed.ScriptingContainer;


public class ThemeSassCompiler {

    private File configLocation;
	private static final String sassLocation = "/portal2/src/main/webapp/themes/sass";
	private static final String cssLocation =  "portal2/src/main/webapp/themes/default/css";

	public ThemeSassCompiler(){
		String path = new java.io.File("").getAbsolutePath();
		if (!path.endsWith("portal")){
			path=path+"/portal";
		}
		String finalPath = path+"/"+cssLocation;
		deleteFolder(new File(finalPath));
	}

	/**
	 * 
	 * Invoked by the exec:maven plugin
	 * 
	 * @args[0] should specify the server path
	 * 
	 * */ 
	public static void main(String[] args) throws Exception{
		
		if(args == null || args.length == 0){
			throw new Exception("Expected arg not present in ThemeSassCompiler");
		}
		try{
			new ThemeSassCompiler().compile(args[0]);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void deleteFolder(File folder){
		File[] css = folder.listFiles();
		if(css != null){
			for(File f : css){
				if(f.isDirectory()) {
					deleteFolder(f);
				}
				else{
					f.delete();
				}
			}			
		} else {
			Logger.getLogger(ThemeSassCompiler.class.getName()).info("Css not found for path!: " + folder.getAbsolutePath());
		}
	}
	
	public void compile(String server){
		

		String path = new java.io.File("").getAbsolutePath();
		if (!path.endsWith("portal")){
			path=path+"/portal";	
		}
		File config = new File(
				(path+ sassLocation).replace("/portal2/portal2", "/portal2"),
				"config.rb");
		
		setConfigLocation(config);
		new ScriptingContainer().runScriptlet(buildInitializationScript(server));
        new ScriptingContainer().runScriptlet(buildCompileScript());
	}

    private String buildCompileScript() {
        StringWriter raw = new StringWriter();
        PrintWriter script = new PrintWriter(raw);

        script.println("Dir.chdir(File.dirname('" + getConfigLocation() + "')) do ");
        script.println("  Compass.compiler.run                                    ");
        script.println("end                                                       ");
        script.flush();

        return raw.toString();
    }

    private String buildInitializationScript(String server) {
    	
    	if(!server.endsWith("/")){
    		server += "/";
    	}
    	
        StringWriter raw = new StringWriter();
        PrintWriter script = new PrintWriter(raw);

        script.println("require 'rubygems'                                                         ");
        script.println("require 'compass'                                                          ");
        script.println("Compass.add_project_configuration '" + getConfigLocation() + "'            ");
        script.println("Compass::Configuration.add_configuration_property(:webserver, \"For when absolute paths are needed\") do ");
        script.println("  \"" + server + "\" ");
        script.println("end ");
        script.println("Compass.configure_sass_plugin!                                             ");
        script.flush();

        return raw.toString();
    }

    private String getConfigLocation() {
        return replaceSlashes(configLocation.getAbsolutePath());
    }

    private String replaceSlashes(String path) {
        return path.replaceAll("\\\\", "/");
    }

    public void setConfigLocation(File configLocation) {
        this.configLocation = configLocation;
    }

}
