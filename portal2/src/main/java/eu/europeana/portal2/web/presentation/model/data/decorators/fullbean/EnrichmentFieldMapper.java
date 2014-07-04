package eu.europeana.portal2.web.presentation.model.data.decorators.fullbean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import eu.europeana.corelib.logging.Logger;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.contextual.ContextualItemDecorator;
import eu.europeana.portal2.web.presentation.model.data.submodel.Resource;

public class EnrichmentFieldMapper {

	Logger log = Logger.getLogger(EnrichmentFieldMapper.class.getCanonicalName());

	private MultilangFieldValue provided;
	private MultilangFieldValue europeana;
	private List<Resource> resources = null;
	private FullBeanWrapper.ContextualEntity entityType;
	private FullBeanLinker document;

	public EnrichmentFieldMapper(FullBeanWrapper.ContextualEntity entityType, FullBeanLinker document) {
		this.entityType = entityType;
		this.document = document;
	}

	public void setProvided(MultilangFieldValue provided) {
		this.provided = provided;
	}

	public void setEuropeana(MultilangFieldValue europeana) {
		this.europeana = europeana;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void createResources() {
		resources = new ArrayList<Resource>();
		if (europeana == null || provided == null) {
			return;
		}

		for (String lang : provided.getLangs()) {
			if (!europeana.has(lang)) {
				break;
			}
			if (provided.getSize(lang) == 1 || europeana.getSize(lang) == 1) {
				if (StringUtils.isNotBlank(provided.get(lang).get(0))
					&& StringUtils.isNotBlank(europeana.get(lang).get(0))) {
					resources.add(
						new Resource(
							provided.get(lang).get(0),
							europeana.get(lang).get(0)
						)
					);
				}
			} else {
				for (String value : provided.get(lang)) {
					if (StringUtils.isBlank(value)) {
						continue;
					}
					for (String uri : europeana.get(lang)) {
						if (StringUtils.isBlank(uri)) {
							continue;
						}
						ContextualItemDecorator entity = findEntityByUri(uri);
						if (entity != null) {
							if (entity.hasAnyLabel(value)) {
								resources.add(new Resource(value, uri));
							}
						} else {
							log.error("without contextual entity!");
						}
					}
				}
			}
		}
	}

	private ContextualItemDecorator findEntityByUri(String uri) {
		ContextualItemDecorator entity = null;
		if (entityType.equals(FullBeanWrapper.ContextualEntity.AGENT)) {
			entity = (ContextualItemDecorator) document.getAgentByURI(uri);
		} else if (entityType.equals(FullBeanWrapper.ContextualEntity.CONCEPT)) {
			entity = (ContextualItemDecorator) document.getConceptByURI(uri);
		} else if (entityType.equals(FullBeanWrapper.ContextualEntity.PLACE)) {
			entity = (ContextualItemDecorator) document.getPlaceByURI(uri);
		} else if (entityType.equals(FullBeanWrapper.ContextualEntity.TIMESPAN)) {
			entity = (ContextualItemDecorator) document.getTimespanByURI(uri);
		}
		return entity;
	}
}
