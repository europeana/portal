package eu.europeana.portal2.web.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class gives you information about fields used in EDM schema.
 * 
 * A field has a qualified schema field name (schemaName), a name we use it in Java classes (propertyName),
 * and Java data type.
 *
 * @author peter.kiraly@kb.nl
 */
public class FieldInfo {

	/**
	 * The list of current Java types of fields
	 */
	private final static List<String> types = new ArrayList<String>(Arrays.asList(
			"String", "ObjectId", "float", "Boolean", "boolean", "DocType",
			"String[]", "Map<String,String>", "List<WebResourceImpl>"
	));

	/**
	 * The collection types
	 */
	private final static List<String> collections = new ArrayList<String>(Arrays.asList(
			"String[]", "Map<String,String>", "List<WebResourceImpl>"
	));

	/**
	 * The qualified schema name
	 */
	private String schemaName;

	/**
	 * The Java property
	 */
	private String propertyName;

	/**
	 * The Java data type
	 */
	private String type;

	/**
	 * The XML element object
	 */
	private Element element;

	public FieldInfo(String schemaName, String propertyName) {
		this(schemaName, propertyName, null);
	}

	public FieldInfo(String schemaName, String propertyName, String type) {
		this.schemaName = schemaName;
		this.propertyName = propertyName;
		this.type = type;
		element = NamespaceResolver.createElement(this.schemaName);
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isCollection() {
		return collections.contains(type);
	}

	public boolean isMap() {
		return type.equals("Map<String,String>");
	}

	public Element getElement() {
		return element;
	}

	public String toString() {
		return "{schemaName: " + schemaName 
				+ ", propertyName: " + propertyName 
				+ ", type: " + type 
				+ ", isCollection(): " + isCollection() + "}";
	}
}
