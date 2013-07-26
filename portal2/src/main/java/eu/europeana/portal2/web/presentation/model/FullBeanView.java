/*
 * Copyright 2007 EDL FOUNDATION
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */

package eu.europeana.portal2.web.presentation.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;

import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.beans.FullBean;

/**
 * todo: javadoc
 * 
 * @author Sjoerd Siebinga <sjoerd.siebinga@gmail.com>
 * @author Gerald de Jong <geralddejong@gmail.com>
 */

public interface FullBeanView extends Serializable {

	DocIdWindowPager getDocIdWindowPager() throws Exception, UnsupportedEncodingException;

	List<? extends BriefBean> getRelatedItems();

	List<? extends BriefBean> getChildren();

	BriefBean getParent();

	List<? extends BriefBean> getParents();

	FullBean getFullDoc();
}