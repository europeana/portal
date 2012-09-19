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

package eu.europeana.portal2.web.presentation.model.data.decorators.lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import eu.europeana.portal2.querymodel.query.FacetCountLink;
import eu.europeana.portal2.web.presentation.model.data.decorators.FacetCountLinkDecorator;

public class FacetCountLinkListDecorator implements List<FacetCountLink> {

	private final Logger log = Logger.getLogger(getClass().getName());

	private String type;
	private List<FacetCountLink> list;
	private List<FacetCountLink> decoratedList;

	public FacetCountLinkListDecorator(String type, List<FacetCountLink> list) {
		this.type = type;
		this.list = list;
		this.decoratedList = null;
	}

	private List<FacetCountLink> asDecoList() {
		if (list == null) {
			return null;
		}
		if (decoratedList == null) {
			decoratedList = new ArrayList();
			for (FacetCountLink facetCountLink : list) {
				decoratedList.add(new FacetCountLinkDecorator(type, facetCountLink));
			}
		}
		return decoratedList;
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public Iterator<FacetCountLink> iterator() {
		return asDecoList().iterator();
	}

	@Override
	public Object[] toArray() {
		if (list != null) {
			return asDecoList().toArray();
		}
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	@Override
	public boolean add(FacetCountLink e) {
		return list.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return list.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends FacetCountLink> c) {
		return list.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends FacetCountLink> c) {
		return list.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public FacetCountLink get(int index) {
		return new FacetCountLinkDecorator(type, list.get(index));
	}

	@Override
	public FacetCountLink set(int index, FacetCountLink element) {
		return list.set(index, element);
	}

	@Override
	public void add(int index, FacetCountLink element) {
		list.add(index, element);
	}

	@Override
	public FacetCountLink remove(int index) {
		return list.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<FacetCountLink> listIterator() {
		return asDecoList().listIterator();
	}

	@Override
	public ListIterator<FacetCountLink> listIterator(int index) {
		return asDecoList().listIterator(index);
	}

	@Override
	public List<FacetCountLink> subList(int fromIndex, int toIndex) {
		return new FacetCountLinkListDecorator(type, list.subList(fromIndex, toIndex));
	}
}
