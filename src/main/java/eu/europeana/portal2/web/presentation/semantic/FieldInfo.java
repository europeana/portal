package eu.europeana.portal2.web.presentation.semantic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class gives you information about fields used in EDM schema.
 * 
 * A field has a qualified schema field name (schemaName), a name we use it in Java classes (propertyName), and Java
 * data type.
 * 
 * @author peter.kiraly@kb.nl
 */
public class FieldInfo {

	/**
	 * The list of current Java types of fields
	 */
//	private final static List<String> types = new ArrayList<String>(Arrays.asList("String", "ObjectId", "float",
//			"Boolean", "boolean", "DocType", "int", "Date", "String[]", "Map<String,String>", "List<WebResourceImpl>",
//			"List<BriefBeanImpl>", "List<Map<String,String>>", "Map<String, List<String>>"));

	/**
	 * The collection types
	 */
	private final static List<String> collections = new ArrayList<String>(Arrays.asList("String[]",
			"Map<String,String>", "List<WebResourceImpl>", "List<BriefBeanImpl>", "List<Map<String,String>>",
			"Map<String, List<String>>"));

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

	private String parent;

	/**
	 * The XML element object
	 */
	private Element element;

	private SchemaOrgMapping schemaOrgMapping;

	public FieldInfo(SchemaOrgMapping mapping, String schemaName, String propertyName, String type, String parent) {
		this.schemaOrgMapping = mapping;
		this.schemaName = schemaName;
		this.propertyName = propertyName;
		this.type = type;
		this.parent = parent;
		element = NamespaceResolver.createElement(this.schemaName);
	}

	public FieldInfo(SchemaOrgMapping mapping, String schemaName, String propertyName, String type) {
		this(mapping, schemaName, propertyName, type, null);
	}

	public FieldInfo(SchemaOrgMapping mapping, String schemaName, String propertyName) {
		this(mapping, schemaName, propertyName, null, null);
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
		return (type != null && collections.contains(type));
	}

	public boolean isMap() {
		return (type != null && type.equals("Map<String,String>"));
	}

	public boolean isCollectionOfMaps() {
		return (type != null && type.equals("List<Map<String,String>>"));
	}

	public boolean isMapsOfLists() {
		return (type != null && type.equals("Map<String,List<String>>"));
	}

	public Element getElement() {
		return element;
	}

	/**
	 * Whether the field may contain multiple values, or only single ones
	 */
	public boolean isMultiValue() {
		return isMap() || isCollection() || isCollectionOfMaps();
	}

	/**
	 * Gets the mapped schema.org element if any, otherwise null.
	 */
	public String getSchemaOrgElement() {
		SchemaOrgElement schemaOrgElement = schemaOrgMapping.get(element);
		if (schemaOrgElement != null) {
			return schemaOrgElement.getElement().getElementName();
		}
		return null;
	}

	/**
	 * Whether the mapped schema.org element is a URL?
	 */
	public boolean getSchemaOrgElementIsURL() {
		SchemaOrgElement schemaOrgElement = schemaOrgMapping.get(element);
		if (schemaOrgElement != null) {
			return schemaOrgElement.getElement().getQualifiedName().equals("schema:url");
		}
		return false;
	}

	@Override
	public String toString() {
		return "{schemaName: " + schemaName + ", propertyName: " + propertyName + ", type: " + type + ", parent: "
				+ parent + ", isCollection(): " + isCollection() + "}";
	}
}
