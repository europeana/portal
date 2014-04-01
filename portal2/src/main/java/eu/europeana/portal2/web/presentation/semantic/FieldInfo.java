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
			"Map<String,List<String>>"));

	/**
	 * The qualified schema name
	 */
	private String schemaName;

	/**
	 * The Java property
	 */
	private String javaPropertyName;

	/**
	 * The Java data type
	 */
	private String javaType;

	private String parent;

	/**
	 * The XML element object
	 */
	private Element element;

	private SchemaOrgMapping schemaOrgMapping;

	/**
	 * 
	 * @param mapping
	 *   Schema.org mapping
	 * @param schemaName
	 *   Schema EDM qualified name
	 * @param javaPropertyName
	 *   Property name in object
	 * @param javaType
	 *   Type name (the name of object such as "FullBean")
	 * @param parent
	 *   Parent object
	 */
	public FieldInfo(SchemaOrgMapping mapping, String schemaName, String javaPropertyName, String javaType, String parent) {
		this.schemaOrgMapping = mapping;
		this.schemaName = schemaName;
		this.javaPropertyName = javaPropertyName;
		this.javaType = javaType;
		this.parent = parent;
		element = ElementFactory.createElement(this.schemaName);
	}

	public FieldInfo(SchemaOrgMapping mapping, String schemaName, String propertyName, String javaType) {
		this(mapping, schemaName, propertyName, javaType, null);
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

	public String getJavaPropertyName() {
		return javaPropertyName;
	}

	public void setJavaPropertyName(String javaPropertyName) {
		this.javaPropertyName = javaPropertyName;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public boolean isCollection() {
		return (javaType != null && collections.contains(javaType));
	}

	public boolean isMap() {
		return (javaType != null && javaType.equals("Map<String,String>"));
	}

	public boolean isCollectionOfMaps() {
		return (javaType != null && javaType.equals("List<Map<String,String>>"));
	}

	public boolean isMapsOfLists() {
		return (javaType != null && javaType.equals("Map<String,List<String>>"));
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
			return schemaOrgElement.isSemanticUrl();
		}
		return false;
	}

	@Override
	public String toString() {
		return "{schemaName: " + schemaName + ", propertyName: " + javaPropertyName + ", type: " + javaType + ", parent: "
				+ parent + ", isCollection(): " + isCollection() + "}";
	}
}
