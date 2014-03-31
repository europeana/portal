package eu.europeana.portal2.web.presentation.semantic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.europeana.corelib.logging.Logger;

public class ElementFactory {

	final static Logger log = Logger.getLogger(ElementFactory.class.getCanonicalName());

	static final Pattern NAME_PATTERN = Pattern.compile("^@?([^:]+):([^:]+)$");

	/**
	 * Return an element object belongs to an XML element
	 * @param element
	 * @return
	 */
	public static Element createElement(String element) {
		Matcher nameMatcher = NAME_PATTERN.matcher(element);
		if (nameMatcher.find()) {
			String prefix = nameMatcher.group(1);
			String elementName = nameMatcher.group(2);
			if (NamespaceResolver.namespaces.containsKey(prefix)) {
				return new Element(NamespaceResolver.namespaces.get(prefix), elementName);
			} else {
				log.warn(String.format("Unregistered namespace prefix: %s", prefix));
			}
		}
		else {
			// log.severe(String.format("Element %s did not match the pattern %s", element, NAME_PATTERN.pattern()));
		}
		return null;
	}
}
