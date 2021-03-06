package eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.contextual;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eu.europeana.corelib.definitions.edm.entity.Agent;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.FullBeanLinker;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.FullBeanWrapper.ContextualEntity;

public class AgentDecorator extends ContextualItemDecorator implements Agent {

	private Agent agent;
	protected ContextualEntity entityType = ContextualEntity.AGENT;

	public AgentDecorator(FullBeanLinker fullBeanLinker, Agent agent,
			String userLanguage, String edmLanguage) {
		super(fullBeanLinker, agent, userLanguage, edmLanguage);
		this.agent = agent;
	}

	@Override
	public Map<String, List<String>> getBegin() {
		return agent.getBegin();
	}

	public List<String> getBeginLang() {
		return getLanguageVersion(agent.getBegin());
	}

	@Override
	public Map<String, List<String>> getEnd() {
		return agent.getEnd();
	}

	public List<String> getEndLang() {
		return getLanguageVersion(agent.getEnd());
	}

	@Override
	public String[] getEdmWasPresentAt() {
		return agent.getEdmWasPresentAt();
	}

	@Override
	public Map<String, List<String>> getEdmHasMet() {
		return agent.getEdmHasMet();
	}

	public List<String> getEdmHasMetLang() {
		return getLanguageVersion(agent.getEdmHasMet());
	}

	@Override
	public Map<String, List<String>> getEdmIsRelatedTo() {
		return agent.getEdmIsRelatedTo();
	}

	public List<String> getEdmIsRelatedToLang() {
		return getLanguageVersion(agent.getEdmIsRelatedTo());
	}

	@Override
	public String[] getOwlSameAs() {
		return agent.getOwlSameAs();
	}

	@Override
	public Map<String, List<String>> getFoafName() {
		return agent.getFoafName();
	}

	@Override
	public Map<String, List<String>> getDcDate() {
		return agent.getDcDate();
	}

	@Override
	public Map<String, List<String>> getDcIdentifier() {
		return agent.getDcIdentifier();
	}

	@Override
	public Map<String, List<String>> getRdaGr2DateOfBirth() {
		return agent.getRdaGr2DateOfBirth();
	}

	public List<String> getRdaGr2DateOfBirthLang() {
		return getLanguageVersion(agent.getRdaGr2DateOfBirth());
	}

	@Override
	public Map<String, List<String>> getRdaGr2DateOfDeath() {
		return agent.getRdaGr2DateOfDeath();
	}

	public List<String> getRdaGr2DateOfDeathLang() {
		return getLanguageVersion(agent.getRdaGr2DateOfDeath());
	}

	@Override
	public Map<String, List<String>> getRdaGr2DateOfEstablishment() {
		return agent.getRdaGr2DateOfEstablishment();
	}

	@Override
	public Map<String, List<String>> getRdaGr2DateOfTermination() {
		return agent.getRdaGr2DateOfTermination();
	}

	@Override
	public Map<String, List<String>> getRdaGr2Gender() {
		return agent.getRdaGr2Gender();
	}

	@Override
	public Map<String, List<String>> getRdaGr2ProfessionOrOccupation() {
		return agent.getRdaGr2ProfessionOrOccupation();
	}

	@Override
	public Map<String, List<String>> getRdaGr2BiographicalInformation() {
		return agent.getRdaGr2BiographicalInformation();
	}

	// SETTERS
	@Override
	public void setBegin(Map<String, List<String>> begin) {}

	@Override
	public void setEnd(Map<String, List<String>> end) {}

	@Override
	public void setEdmWasPresentAt(String[] edmWasPresentAt) {}

	@Override
	public void setEdmIsRelatedTo(Map<String, List<String>> edmIsRelatedTo) {}

	@Override
	public void setEdmHasMet(Map<String, List<String>> edmHasMet) {}

	@Override
	public void setOwlSameAs(String[] owlSameAs) {}

	@Override
	public void setFoafName(Map<String, List<String>> foafName) {}

	@Override
	public void setDcDate(Map<String, List<String>> dcDate) {}

	@Override
	public void setDcIdentifier(Map<String, List<String>> dcIdentifier) {}

	@Override
	public void setRdaGr2DateOfBirth(Map<String, List<String>> rdaGr2DateOfBirth) {}

	@Override
	public void setRdaGr2DateOfDeath(Map<String, List<String>> rdaGr2DateOfDeath) {}

	@Override
	public void setRdaGr2DateOfEstablishment(Map<String, List<String>> rdaGr2DateOfEstablishment) {}

	@Override
	public void setRdaGr2DateOfTermination(Map<String, List<String>> rdaGr2DateOfTermination) {}

	@Override
	public void setRdaGr2Gender(Map<String, List<String>> rdaGr2Gender) {}

	@Override
	public void setRdaGr2ProfessionOrOccupation(Map<String, List<String>> rdaGr2ProfessionOrOccupation) {}

	@Override
	public void setRdaGr2BiographicalInformation(Map<String, List<String>> rdaGr2BiographicalInformation) {}

	public void makeLinks(Map<String, String> ids) {
	}

	@Override
	public List<ContextualItemDecorator> getRelatedContextualItem() {
		List<ContextualItemDecorator> items = new ArrayList<ContextualItemDecorator>();
		return items;
	}

	@Override
	public ContextualEntity getEntityType() {
		return entityType;
	}

	@Override
	public void setRdaGr2PlaceOfDeath(
			Map<String, List<String>> rdaGr2PlaceOfDeath) {
		
		
	}

	@Override
	public Map<String, List<String>> getRdaGr2PlaceOfDeath() {
		
		return agent.getRdaGr2PlaceOfDeath();
	}

	@Override
	public void setRdaGr2PlaceOfBirth(
			Map<String, List<String>> rdaGr2PlaceOfBirth) {
		
	}

	@Override
	public Map<String, List<String>> getRdaGr2PlaceOfBirth() {
		// TODO Auto-generated method stub
		return agent.getRdaGr2PlaceOfBirth();
	}
}
