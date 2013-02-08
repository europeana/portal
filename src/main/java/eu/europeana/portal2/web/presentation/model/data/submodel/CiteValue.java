/*
 * Copyright 2007-2012 The Europeana Foundation
 *
 *  Licenced under the EUPL, Version 1.1 (the "Licence") and subsequent versions as approved 
 *  by the European Commission;
 *  You may not use this work except in compliance with the Licence.
 *  
 *  You may obtain a copy of the Licence at:
 *  http://joinup.ec.europa.eu/software/page/eupl
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under 
 *  the Licence is distributed on an "AS IS" basis, without warranties or conditions of 
 *  any kind, either express or implied.
 *  See the Licence for the specific language governing permissions and limitations under 
 *  the Licence.
 */

package eu.europeana.portal2.web.presentation.model.data.submodel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import eu.europeana.corelib.utils.StringArrayUtils;
import eu.europeana.portal2.web.presentation.enums.CiteStyle;
import eu.europeana.portal2.web.presentation.model.data.FullDocData;

public class CiteValue {

	private final Logger log = Logger.getLogger(CiteValue.class.getCanonicalName());

	private FullDocData model;

	private CiteStyle value;

	public CiteValue(FullDocData model, CiteStyle value) {
		this.model = model;
		this.value = value;
	}

	public String getLabel() {
		return value.getLabel();
	}

	@Override
	public String toString() {
		return value.toString();
	}

	/**
	 * Creates the citation text
	 * 
	 * @param citeStyle
	 *            - What cite style to use
	 * @param locale
	 *            - users locale
	 * @return - Citation in format of users locale
	 */
	public String getCiteText() {
		String authorNames = StringArrayUtils.formatList(model.getShortcut().get("DcCreator"));
		StringBuilder citeStyleText = new StringBuilder();

		switch (value) {
			case WIKIPEDIA:
				citeStyleText.append("{{cite web | url=");
				citeStyleText.append(model.getDocument().getId());
				citeStyleText.append("|title=");

				if( StringArrayUtils.isNotBlank(model.getDocument().getTitle())){
					citeStyleText.append( model.getDocument().getTitle()[0]);
				}
				else{
					citeStyleText.append(model.getPageTitle().replace("|", ""));
				}

				if (StringUtils.isNotBlank(authorNames)) {
					citeStyleText.append("|author=" + authorNames);
				}

				citeStyleText.append("|accessdate=");
				citeStyleText.append(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
				citeStyleText.append(" |publisher=Europeana}}");
				break;
			case HARVARD:
				String availableAtMsg = "Webpage available at:";
				if (authorNames != null) {
					citeStyleText.append(authorNames).append(" (");
				} else {
					citeStyleText.append("(");
				}
				// citeStyleText.append(model.getDocument().getEuropeanaYear()[0])
				if (model.getDocument().getYear() != null) {
					citeStyleText.append(model.getDocument().getYear()[0]);
				}
				citeStyleText.append(") ").append(model.getPageTitle()).append(" ");
				citeStyleText.append(availableAtMsg).append(" ");
				citeStyleText.append(model.getDocument().getId()).append(" [Accessed: ");
				citeStyleText.append(new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a").format(Calendar.getInstance().getTime()));
				citeStyleText.append("]");
				break;
		}
		return citeStyleText.toString();
	}
}
