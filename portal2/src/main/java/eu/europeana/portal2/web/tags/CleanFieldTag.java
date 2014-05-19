package eu.europeana.portal2.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import eu.europeana.portal2.web.presentation.model.data.submodel.FieldCleaner;

public class CleanFieldTag extends SimpleTagSupport {

	private String text;

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void doTag() throws JspException, IOException {
		getJspContext().getOut().print(FieldCleaner.clean(text));
	}

}
