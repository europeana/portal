package eu.europeana.portal2.web.presentation.semantic;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class SchemaOrgElement {

	private static final String SCHEMA_URL = "schema:url";

	private Element element;
	private List<Element> parents;
	private Element edmElement;
	private Element edmParent;

	public SchemaOrgElement(String element) {
		this(element, new String[]{});
	}

	public SchemaOrgElement(String elementName, String[] parents) {
		this.element = ElementFactory.createElement(elementName);
		setParents(parents);
	}

	public SchemaOrgElement(String element, String[] parents, String edmElement) {
		this(element, parents);
		this.edmElement = ElementFactory.createElement(edmElement);
	}

	public SchemaOrgElement(String element, String[] parents, String edmElement, String edmParent) {
		this(element, parents, edmElement);
		this.edmParent = ElementFactory.createElement(edmParent);
	}

	public Element getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = ElementFactory.createElement(element);
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
			if (StringUtils.isNotBlank(parent)) {
				Element parentElement = ElementFactory.createElement(parent);
				if (parentElement != null) {
					this.parents.add(parentElement);
				}
			}
		}
	}

	public void setParents(List<Element> parents) {
		this.parents = parents;
	}

	public Element getEdmElement() {
		return edmElement;
	}

	public Element getEdmParent() {
		return edmParent;
	}

	public void setEdmParent(Element edmParent) {
		this.edmParent = edmParent;
	}

	public boolean isSemanticUrl() {
		return StringUtils.equals(element.getQualifiedName(), SCHEMA_URL);
	}

	public void setEdmElement(Element edmElement) {
		this.edmElement = edmElement;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((element == null) ? 0 : element.hashCode());
		result = prime * result + parents.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SchemaOrgElement other = (SchemaOrgElement) obj;
		if (element == null) {
			if (other.element != null)
				return false;
		} else if (!element.equals(other.element))
			return false;
		if (parents == null) {
			if (other.parents != null)
				return false;
		} else if (!parents.equals(other.parents))
			return false;
		if (edmElement == null) {
			if (other.edmElement != null)
				return false;
		} else if (!edmElement.equals(other.edmElement))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SchemaOrgElement [element=" + element + ", parents=" + parents
				+ ", edmElement=" + edmElement + ", edmParent=" + edmParent + "]";
	}
}