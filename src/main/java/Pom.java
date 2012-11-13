import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class Pom {

	private final String root		= "/src/main/webapp/themes/default/";
	private final String webAppTgt	= "src/main/webapp/themes/default/";
	private final String pomName	= "pom-generated.xml";
	private final String cleanup	= "cleanup-minified.sh";
	private int count = 0;
	
	private final String[] includes = {
			"css/",
			"js/com/gmtplusone",
			"js/com/",
			"js/scottjehl-iOS-Orientationchange-Fix-99c9c99",
			"js/eu",
			"js/galleria/themes",
			"js/js/"			
	};
	private final String[] excludes = {			
			"css/behaviour/",
			"/min",
			"/galleria/themes/classic"
	};
	
	public static void main(String[] args) {
		try{
			new Pom();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public Pom() throws IOException{
		
		File f = new File(pomName);
		if(f.exists()){
			f.delete();
		}
		f.createNewFile();			

		f = new File(cleanup);
		if(f.exists()){
			f.delete();
		}
		f.createNewFile();
		
		
		File folder = new File(new java.io.File( "." ).getCanonicalPath() + root);
	    
		openXml();
		writeToFile("<!-- js files -->");

		this.search(folder, ".js");
	    
		writeToFile("<!-- css files -->");

	    this.search(folder, ".css");

	    closeXml();
		System.out.println("Finished generating '" + pomName + "'");
		System.out.println("Now run maven with the command:");
		System.out.println("\t\tmvn -f " + pomName + "  minify:minify package");
	}
	
	private void makePomEntry(String path, String name, String suffix){

		suffix = suffix.replace(".", "");
		String srcDir = path.substring(path.indexOf("webapp") + "webapp".length(), path.length()).replace("/" + name, "");
		String tgtDir = "";
		
		if( srcDir.indexOf("/" + suffix) < 0 ){
			if(suffix == "css"){
				tgtDir = srcDir.substring(	srcDir.indexOf("/css") + 1 );				
			}
		}
		else{
			tgtDir = srcDir.substring(	srcDir.indexOf("/" + suffix) + 1 );
		}
		tgtDir += "/min";

		writeToFile("<execution>");
		writeToFile(	"\t<id>minify-" + count + "-" + name + "</id>");
		writeToFile(	"\t<phase>process-resources</phase>");
		writeToFile(	"\t<configuration>");
		
		writeToFile(		"\t\t<webappTargetDir>" + webAppTgt + "</webappTargetDir>");
		writeToFile(		"\t\t<warSourceExcludes>" + name + "</warSourceExcludes>");
		writeToFile(		"\t\t<" + suffix + "SourceDir>" + srcDir + "</" + suffix + "SourceDir>");
		writeToFile(		"\t\t<" + suffix + "SourceIncludes>");
		writeToFile(			"\t\t\t<" + suffix + "SourceInclude>" + name + "</" + suffix + "SourceInclude>");
		writeToFile(		"\t\t</" + suffix + "SourceIncludes>");

		writeToFile(		"\t\t<" + suffix + "TargetDir>" + tgtDir + "</" + suffix + "TargetDir>");
		writeToFile(		"\t\t<" + suffix + "FinalFile>" + name + "</" + suffix + "FinalFile>");
        
        
		writeToFile(	"\t</configuration>");
		writeToFile(	"\t<goals>");
		writeToFile(		"\t\t<goal>minify</goal>");
		writeToFile(	"\t</goals>");
		writeToFile("</execution>");
		
		writeToCleanup(webAppTgt + tgtDir);
		count ++;
	}
	
	private void openXml(){
		writeToFile("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writeToFile("<project");
		writeToFile(	"\txmlns=\"http://maven.apache.org/POM/4.0.0\"");
		writeToFile(	"\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		writeToFile(	"\txsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">");
		writeToFile(	"\t<modelVersion>4.0.0</modelVersion>");
		writeToFile(	"\t<groupId>com.samaxes.maven</groupId>");
		writeToFile(	"\t<artifactId>minify-maven-plugin-demo</artifactId>");
		writeToFile(	"\t<version>1.5.2</version>");
		writeToFile(	"\t<packaging>war</packaging>");
		writeToFile(	"\t<name>Minify Europeana Resources</name>");
		writeToFile(	"\t<description>Portal 2 resource minification.</description>");
		writeToFile(	"\t<url>https://github.com/samaxes/minify-maven-plugin</url>");
		writeToFile(	"\t<build>");
		writeToFile(		"\t\t<plugins>");
		writeToFile(			"\t\t\t<plugin>");
		writeToFile(				"\t\t\t\t<groupId>org.apache.maven.plugins</groupId>");
		writeToFile(				"\t\t\t\t<artifactId>maven-compiler-plugin</artifactId>");
		writeToFile(				"\t\t\t\t<version>2.5.1</version>");
		writeToFile(				"\t\t\t\t<configuration>");
		writeToFile(					"\t\t\t\t\t<source>${maven.compiler.source}</source>");
		writeToFile(         			"\t\t\t\t\t<target>${maven.compiler.source}</target>");
		writeToFile(               		"\t\t\t\t\t<encoding>${project.build.sourceEncoding}</encoding>");
		writeToFile(                "\t\t\t\t</configuration>");
		writeToFile(            "\t\t\t</plugin>");
		writeToFile(            "\t\t\t<plugin>");
		writeToFile(                "\t\t\t\t<groupId>org.apache.maven.plugins</groupId>");
		writeToFile(                "\t\t\t\t<artifactId>maven-resources-plugin</artifactId>");
		writeToFile(                "\t\t\t\t<version>2.6</version>");
		writeToFile(                "\t\t\t\t<configuration>");
		writeToFile(                	"\t\t\t\t\t<encoding>${project.build.sourceEncoding}</encoding>");
		writeToFile(            	"\t\t\t\t</configuration>");
		writeToFile(           	"\t\t\t</plugin>");
		writeToFile(           	"\t\t\t<plugin>");
		writeToFile(           		"\t\t\t\t<groupId>org.apache.maven.plugins</groupId>");
		writeToFile(                "\t\t\t\t<artifactId>maven-assembly-plugin</artifactId>");
		writeToFile(                "\t\t\t\t<version>2.3</version>");
		writeToFile(                "\t\t\t\t<configuration>");
		writeToFile(                	"\t\t\t\t\t<descriptorRefs>");
		writeToFile(                    	"\t\t\t\t\t\t<descriptorRef>src</descriptorRef>");
		writeToFile(                  	"\t\t\t\t\t</descriptorRefs>");
		writeToFile(               "\t\t\t\t</configuration>");
		writeToFile(           "\t\t\t</plugin>");
		writeToFile(           "\t\t\t<plugin>");
		writeToFile(               	"\t\t\t\t<!-- mvn assembly:single ghDownloads:upload -->");
		writeToFile(               	"\t\t\t\t<groupId>com.github.github</groupId>");
		writeToFile(               	"\t\t\t\t<artifactId>downloads-maven-plugin</artifactId>");
		writeToFile(               	"\t\t\t\t<version>0.6</version>");
		writeToFile(               	"\t\t\t\t<configuration>");
		writeToFile(                	"\t\t\t\t\t<includes>");
		writeToFile(                    	"\t\t\t\t\t\t<includes>${project.artifactId}-${project.version}-src.zip</includes>");
		writeToFile(                    "\t\t\t\t\t</includes>");
		writeToFile(               	    "\t\t\t\t\t<override>true</override>");
		writeToFile(               	"\t\t\t\t</configuration>");
		writeToFile(            "\t\t\t</plugin>");
		writeToFile(            "\t\t\t<plugin>");
		writeToFile(               	"\t\t\t\t<groupId>org.apache.maven.plugins</groupId>");
		writeToFile(                "\t\t\t\t<artifactId>maven-war-plugin</artifactId>");
		writeToFile(               	"\t\t\t\t<version>2.2</version>");
		writeToFile(               	"\t\t\t\t<configuration>");
		writeToFile(                	"\t\t\t\t\t<archive>");
		writeToFile(                    	"\t\t\t\t\t\t<manifest>");
		writeToFile(                        	"\t\t\t\t\t\t\t<addDefaultImplementationEntries>true</addDefaultImplementationEntries>");
		writeToFile(                    	"\t\t\t\t\t\t</manifest>");
		writeToFile(                        "\t\t\t\t\t\t<addMavenDescriptor>false</addMavenDescriptor>");
		writeToFile(                	"\t\t\t\t\t</archive>");
		writeToFile(                "\t\t\t\t</configuration>");
		writeToFile(            "\t\t\t</plugin>");
		writeToFile(            "\t\t\t<!-- START THE MINIFY-TO-SOURCE MINIFIER -->");
		writeToFile(            "\t\t\t<plugin>");
		writeToFile(            	"\t\t\t\t<groupId>com.samaxes.maven</groupId>");
		writeToFile(               	"\t\t\t\t<artifactId>minify-maven-plugin</artifactId>");
		writeToFile(                "\t\t\t\t<version>1.5.2</version>");
		writeToFile(                "\t\t\t\t<executions>");
	}
	
	
	private void closeXml(){
		writeToFile(				"\t\t\t\t</executions>");
		writeToFile(			"\t\t\t</plugin>");
		writeToFile(			"\t\t\t</plugins>");
		writeToFile(		"\t\t</build>");
		writeToFile(		"\t\t<properties>");
		writeToFile(			"\t\t\t<github.global.server>github</github.global.server>");
		writeToFile(			"\t\t\t<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>");
		writeToFile(			"\t\t\t<project.build.resourceEncoding>ISO-8859-1</project.build.resourceEncoding>");
		writeToFile(			"\t\t\t<maven.compiler.target>1.5</maven.compiler.target>");
		writeToFile(			"\t\t\t<maven.compiler.source>1.5</maven.compiler.source>");
		writeToFile(		"\t\t</properties>");
		writeToFile(	"\t</project>");
	}
	

	private void writeToCleanup(String s){		
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(cleanup, true)));
		    out.println("rm -rf " + s);
		    out.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	private void writeToFile(String s){
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(pomName, true)));
		    out.println(s);
		    out.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
    private void search(File dir, String suffix) throws IOException {

    	File[] files = dir.listFiles();
    	
    	for (int i=0; i < files.length; i++) { 	

    		File f = files[i];
    		
    		if(!f.isHidden()){
	    		if(f.isDirectory()){
		    		if(f.getName() != "min"){
		    			search(f, suffix);
		    		}
	        	}
	        	else{
	        		if(f.getName().endsWith(suffix)){ // check in includes
	        			
	        			String path = f.getCanonicalPath();
	        			boolean cleared = false;
	        			for(int j=0; j < includes.length; j++){
	        				if(path.indexOf(root + includes[j]) > -1 ){
	        					cleared = true;
	        				}
	        			}
	        			if(cleared){  // check not in excludes
	        				
		        			boolean blocked = false;
		        			for(int j=0; j < excludes.length; j++){
		        				//if(path.indexOf(root + excludes[j]) > -1 ){
		        				if(path.indexOf(excludes[j]) > -1 ){
		        					blocked = true;
		        				}
		        			}
		        			
		        			if(!blocked){
		        				
		        				if(!path.endsWith(".min" + suffix)){ 					
					        		System.out.println("RESULT: " + path + "   " );
					        		makePomEntry(path, f.getName(), suffix);
		        				}
		        			}
	        			}
	        		}
	        	}
    		}
    	}

    }
}
