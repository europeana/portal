package eu.europeana.portal2.web.configuration;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Registering the static page path as web resource resolver. It means, that static files of the static pages (js, css,
 * img) will served directly, and they won't get through a contoller.
 * 
 * @author peter.kiraly@kb.nl
 */
@EnableWebMvc
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

	@Resource
	private eu.europeana.portal2.services.Configuration config;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String path = "file:" + config.getStaticPagePath();
		path += (path.endsWith("/")) ? "sp" : "/sp";

		registry.addResourceHandler("/img/**").addResourceLocations(path + "/img/");
		registry.addResourceHandler("/css/**").addResourceLocations(path + "/css/");
		registry.addResourceHandler("/js/**").addResourceLocations(path + "/js/");

		path = "file:" + config.getResponsiveCache();
		path += (path.endsWith("/")) ? "" : "/";

		registry.addResourceHandler("/rss-blog-cache/**").addResourceLocations(path);
	}
}
