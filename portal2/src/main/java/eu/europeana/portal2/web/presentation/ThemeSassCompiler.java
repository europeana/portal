package eu.europeana.portal2.web.presentation;

import java.io.File;

import org.jruby.embed.ScriptingContainer;

import java.io.PrintWriter;
import java.io.StringWriter;


public class ThemeSassCompiler {

    private File configLocation;
	private static final String sassLocation = "/portal2/src/main/webapp/themes/sass";
	
	/**
	 * 
	 * Invoked by the exec:maven plugin
	 * 
	 * @args[0] should specify the server path
	 * 
	 * */ 
	public static void main(String[] args) throws Exception{
		
		if(args == null || args.length==0){
			throw new Exception("Expected arg not present in ThemeSassCompiler");
		}
		try{
			new ThemeSassCompiler().compile(args[0]);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void compile(String server){
		File config = new File(
				(new java.io.File("").getAbsolutePath() + sassLocation).replace("/portal2/portal2", "/portal2"),
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
        //script.println("  Compass.sass_compiler.run                               ");

        script.println("end                                                       ");
        script.flush();

        return raw.toString();
    }

    private String buildInitializationScript(String server) {
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
