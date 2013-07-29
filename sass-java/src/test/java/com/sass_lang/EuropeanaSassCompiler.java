package com.sass_lang;

import javax.servlet.*;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.sass_lang.SassCompilingFilter.*;

public class EuropeanaSassCompiler {
	
    private SassCompilingFilter filter;

    public static void main(String[] args){
    	try{
    		new EuropeanaSassCompiler().go();    		
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }

    public void go() throws Exception {
    	
        FilterConfig filterConfig = new FilterConfig() {
    		Map<String,String> vec = new HashMap<String,String>();
    		{
    			vec.put(CONFIG_LOCATION_PARAM, "config.rb");
    		};
    		
			public ServletContext getServletContext() {
				return new ServletContext() {
					
					public String getRealPath(String arg0) {
						
						System.err.println("get the root: " + new java.io.File("").getAbsolutePath() );
						/*
						/Users/admin/Documents/PortalWorkspace/javaSass/sass-java
						 
						/Users/admin/Documents/PortalWorkspace/javaSass/sass-java
						*/
						return new java.io.File("").getAbsolutePath() + "/src/test/sample-webapp/WEB-INF/sass";
					}

					public void setAttribute(String arg0, Object arg1) {
					}
					
					public void removeAttribute(String arg0) {
					}
					
					public void log(String arg0, Throwable arg1) {
					}
					
					public void log(Exception arg0, String arg1) {
					}
					
					public void log(String arg0) {
					}
					
					public Enumeration getServlets() {
						return null;
					}
					
					public Enumeration getServletNames() {
						return null;
					}
					
					public String getServletContextName() {
						return null;
					}
					
					public Servlet getServlet(String arg0) throws ServletException {
						return null;
					}
					
					public String getServerInfo() {
						return null;
					}
					
					public Set getResourcePaths(String arg0) {
						return null;
					}
					
					public InputStream getResourceAsStream(String arg0) {
						return null;
					}
					
					public URL getResource(String arg0) throws MalformedURLException {
						return null;
					}
					
					public RequestDispatcher getRequestDispatcher(String arg0) {
						return null;
					}
					
					public RequestDispatcher getNamedDispatcher(String arg0) {
						return null;
					}
					
					public int getMinorVersion() {
						return 0;
					}
					
					public String getMimeType(String arg0) {
						return null;
					}
					
					public int getMajorVersion() {
						return 0;
					}
					
					public Enumeration getInitParameterNames() {
						return null;
					}
					
					public String getInitParameter(String arg0) {
						return null;
					}
					
					public String getContextPath() {
						return null;
					}
					
					public ServletContext getContext(String arg0) {
						return null;
					}
					
					public Enumeration getAttributeNames() {
						return null;
					}
					
					public Object getAttribute(String arg0) {
						return null;
					}
				};
			}
			
			public Enumeration getInitParameterNames() {
				return null;
			}
			
			public String getInitParameter(String arg0) {
				return vec.get(arg0);
			}
			
			public String getFilterName() {
				return null;
			}
		};

        filter = new SassCompilingFilter();
        filter.init(filterConfig);
        
        try{
        	filter.doFilter(null, null, null);
        } catch (NullPointerException e){
        	//do nothing
        }
    }

}
