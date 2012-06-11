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

import eu.europeana.corelib.definitions.model.web.BreadCrumb;
import eu.europeana.portal2.web.presentation.model.data.SearchData;
import eu.europeana.portal2.web.presentation.model.data.decorators.BreadcrumbDecorator;

public class BreadcrumbListDecorator implements List<BreadCrumb> {
	
	private static final Logger log = Logger.getLogger(BreadcrumbListDecorator.class.getName());

	private SearchData model;
	private List<BreadCrumb> list;

	public BreadcrumbListDecorator(SearchData model, List<BreadCrumb> list) {
		this.model = model;
		this.list = list;
	}

	public List<BreadcrumbDecorator> asDecoList() {
		if (list == null) {
			return null;
		}
		List<BreadcrumbDecorator> newList = new ArrayList<BreadcrumbDecorator>();
		for (BreadCrumb breadcrumb : list) {
			newList.add(new BreadcrumbDecorator(model, breadcrumb));
		}
		return newList;
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
	public Iterator<BreadCrumb> iterator() {
		return list.iterator();
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
	public boolean add(BreadCrumb e) {
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
	public boolean addAll(Collection<? extends BreadCrumb> c) {
		return list.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends BreadCrumb> c) {
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
	public BreadCrumb get(int index) {
		return new BreadcrumbDecorator(model, list.get(index));
	}

	@Override
	public BreadCrumb set(int index, BreadCrumb element) {
		return list.set(index, element);
	}

	@Override
	public void add(int index, BreadCrumb element) {
		list.add(index, element);
	}

	@Override
	public BreadCrumb remove(int index) {
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
	public ListIterator<BreadCrumb> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<BreadCrumb> listIterator(int index) {
		return list.listIterator(index);
	}

	@Override
	public List<BreadCrumb> subList(int fromIndex, int toIndex) {
		return new BreadcrumbListDecorator(model, list.subList(fromIndex,
				toIndex));
	}

}
