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
}
