package eu.europeana.corelib.solr.service.mock.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bson.types.ObjectId;

import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.entity.WebResource;
import eu.europeana.corelib.solr.bean.impl.FullBeanImpl;
import eu.europeana.corelib.solr.entity.AggregationImpl;
import eu.europeana.corelib.solr.entity.EuropeanaAggregationImpl;
import eu.europeana.corelib.solr.entity.ProvidedCHOImpl;
import eu.europeana.corelib.solr.entity.ProxyImpl;
import eu.europeana.corelib.solr.entity.WebResourceImpl;

public class FullBeanMock extends FullBeanImpl {

	
	public FullBeanMock(BriefBean brief){
		this.setEuropeanaId( ObjectId.massageToObjectId(brief.getId()) );
		this.setType(brief.getType());
		this.setTitle(brief.getTitle());
		
		
		this.about = brief.getId();
		
		List<ProxyImpl> proxies = new ArrayList<ProxyImpl>();
		
		ProxyImpl proxy1 = new ProxyImpl();
		proxy1.setId(ObjectId.massageToObjectId(brief.getId()));
		proxy1.setAbout(brief.getId());
		proxy1.setEdmType(brief.getType());
		proxies.add(proxy1);
		
		ProxyImpl proxy2 = new ProxyImpl();
		proxy2.setId(ObjectId.massageToObjectId(brief.getId()));
		proxy2.setAbout(brief.getId());
		proxy2.setEdmType(brief.getType());
  		proxies.add(proxy2);

  		this.proxies = proxies;
	
  		
  		this.europeanaAggregation = new EuropeanaAggregationImpl();
  		
  		HashMap<String, List<String>> edmCountry =  new HashMap<String, List<String>>();
  		edmCountry.put("def", Arrays.asList(new String[]{ "Italy" } ) );
  		
  		
  		
  		this.europeanaAggregation.setEdmCountry(edmCountry);
  		this.europeanaAggregation.setEdmLandingPage("");
  		
  		List<AggregationImpl> aggregations= new ArrayList<AggregationImpl>();
  		AggregationImpl aggregation = new AggregationImpl();
  		aggregation.setAbout(brief.getId());
  		
  		
  		List<WebResource> wResources = new ArrayList<WebResource>();
  		WebResource wResource = new WebResourceImpl();
  		wResource.setAbout(brief.getId());
  		wResources.add(wResource);
  		aggregation.setWebResources(wResources);
  		aggregations.add(aggregation);
  		
  		this.aggregations = aggregations;

  		  		
  		List<ProvidedCHOImpl> providedCHOs = new ArrayList<ProvidedCHOImpl>();
  		providedCHOs.add(new ProvidedCHOImpl());

  		this.providedCHOs = providedCHOs;
  		
  		
  		this.country = new String[]{"Italy"};
  		
  		
  		
	}


}
