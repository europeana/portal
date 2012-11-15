package eu.europeana.portal2.web.presentation.semantic;

import java.util.Map;


/**
 * Representation of an XML element
 * 
 * @author peter.kiraly@kb.nl
 */
public class Element {

	/**
	 * Namespace
	 */
	private Namespace namespace;

	/**
	 * Element name
	 */
	private String elementName;

	/**
	 * Attributes
	 */
	private Map<String, String> attributes;

	public Element(Namespace namespace, String elementName) {
		this.namespace = namespace;
		this.elementName = elementName;
	}

	public Namespace getNamespace() {
		return namespace;
	}

	public void setNamespace(Namespace namespace) {
		this.namespace = namespace;
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public String getPrefix() {
		return namespace.getPrefix();
	}

	/**
	 * Gets the fully qualified name, like http://www.europeana.eu/schemas/edm/ProvidedCHO
	 *
	 * @return
	 *   The fully qualified name
	 */
	public String getFullQualifiedURI() {
		return namespace.getUri() + elementName;
	}

	/**
	 * Gets the qualified name like edm:ProvidedCHO
	 * @return
	 *   The qualified name
	 */
	public String getQualifiedName() {
		return namespace.getPrefix() + ":" + elementName;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result
				+ ((elementName == null) ? 0 : elementName.hashCode());
		result = prime * result
				+ ((namespace == null) ? 0 : namespace.hashCode());
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
		Element other = (Element) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		if (elementName == null) {
			if (other.elementName != null)
				return false;
		} else if (!elementName.equals(other.elementName))
			return false;
		if (namespace == null) {
			if (other.namespace != null)
				return false;
		} else if (!namespace.equals(other.namespace))
			return false;
		return true;
	}
}
