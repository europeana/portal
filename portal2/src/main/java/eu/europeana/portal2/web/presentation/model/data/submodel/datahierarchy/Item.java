package eu.europeana.portal2.web.presentation.model.data.submodel.datahierarchy;

public abstract class Item {

	protected String name;
	protected long count;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
}
