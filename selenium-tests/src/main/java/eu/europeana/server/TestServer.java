package eu.europeana.server;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

import java.io.File;
import java.io.IOException;


import org.openqa.selenium.remote.server.DriverServlet;


public class TestServer {


	
	public static void main(String[] args){
		new TestServer();
	}
	
	private static File getAppRoot() {
		try {
			return new File(".").getCanonicalFile();
		} catch (IOException e) {
			throw new RuntimeException("Couldn't get canonical file", e);
		}
	}
	
	private int getPortNo(){
		
		int portNo = 4444;
		try{
			Ini ini = new Ini( new File( getAppRoot() + File.separator + "start.ini" ) );
			portNo = Integer.parseInt( ini.get("server", "remoteWebDriverPort").trim().replace(";", "") );
		}
		catch(InvalidFileFormatException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		//JOptionPane.showMessageDialog(new JPanel(), "portNo = " + portNo);
		
		return portNo;
	}
	
	  
	public TestServer(){
		
		Server server = new Server();
		server.setStopAtShutdown(true);
		
		try {

		    WebAppContext context = new WebAppContext();
		    context.setContextPath("");
		    context.setWar( "." );
		    server.addHandler(context);
		    context.addServlet(DriverServlet.class, "/wd/*");

		    SelectChannelConnector connector = new SelectChannelConnector();
		    
		    connector.setPort(getPortNo());
		    
		    server.addConnector(connector);
			
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}
		
	
	
}
