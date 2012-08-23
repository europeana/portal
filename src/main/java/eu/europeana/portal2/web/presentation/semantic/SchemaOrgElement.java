package eu.europeana.portal2.web.presentation.semantic;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class SchemaOrgElement {

	private Element element;
	private List<Element> parents;

	public SchemaOrgElement(String element, String[] parents) {
		this.element = NamespaceResolver.createElement(element);

		this.parents = new LinkedList<Element>();
		for (String parent : parents) {
			if (StringUtils.isBlank(parent)) {
				this.parents.add(NamespaceResolver.createElement(parent));
			}
		}
	}

	public Element getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = NamespaceResolver.createElement(element);
	}

	public void setElement(Element element) {
		this.element = element;
	}

	public List<Element> getParents() {
		return parents;
	}

	public void setParents(String[] parents) {
		this.parents = new LinkedList<Element>();
		for (String parent : parents) {
			if (StringUtils.isBlank(parent)) {
				this.parents.add(NamespaceResolver.createElement(parent));
			}
		}
	}

	public void setParents(List<Element> parents) {
		this.parents = parents;
	}
}
