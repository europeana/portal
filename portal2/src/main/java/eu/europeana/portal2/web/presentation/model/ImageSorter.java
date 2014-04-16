package eu.europeana.portal2.web.presentation.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import eu.europeana.portal2.web.presentation.model.submodel.SortableImage;

public class ImageSorter<T extends SortableImage> {

	Map<String, T> aboutIndex = new HashMap<String, T>();
	Map<String, T> sequenceIndex = new HashMap<String, T>();
	List<T> imagesToShow;
	LinkedList<T> items = new LinkedList<T>();
	T firstImg;

	public ImageSorter(List<T> imagesToShow) {
		this.imagesToShow = new LinkedList<T>(imagesToShow);
	}

	public List<T> sort() {
		createIndexes();
		if (firstImg != null) {
			items.add(firstImg);
			imagesToShow.remove(firstImg);
			findPrecedingSiblings(firstImg);
			findFollowingSiblings(firstImg);
			imagesToShow.addAll(items);
		}
		return imagesToShow;
	}

	private void findPrecedingSiblings(SortableImage img) {
		if (StringUtils.isNotBlank(img.getIsNextInSequence())) {
			if (aboutIndex.containsKey(img.getIsNextInSequence())) {
				T preceding = aboutIndex.get(img.getIsNextInSequence());
				if (preceding != null) {
					if (!items.contains(preceding)) {
						imagesToShow.remove(preceding);
						items.add(0, preceding);
						findPrecedingSiblings(preceding);
					}
				}
			}
		}
	}

	private void findFollowingSiblings(SortableImage img) {
		if (StringUtils.isNotBlank(img.getAbout())) {
			if (sequenceIndex.containsKey(img.getAbout())) {
				T following = sequenceIndex.get(img.getAbout());
				if (following != null) {
					if (!items.contains(following)) {
						imagesToShow.remove(following);
						items.add(following);
						findFollowingSiblings(following);
					}
				}
			}
		}
	}

	private void createIndexes() {
		for (T img : imagesToShow) {
			if (StringUtils.isNotBlank(img.getIsNextInSequence())) {
				firstImg = img;
				sequenceIndex.put(img.getIsNextInSequence(), img);
			}
			if (StringUtils.isNotBlank(img.getAbout())) {
				aboutIndex.put(img.getAbout(), img);
			}
		}
	}
}
