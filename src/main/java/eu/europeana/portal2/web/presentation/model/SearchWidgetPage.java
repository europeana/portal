package eu.europeana.portal2.web.presentation.model;

import java.util.List;

import eu.europeana.portal2.web.presentation.enums.ResultTypeTab;
import eu.europeana.portal2.web.presentation.model.data.SearchWidgetData;

public class SearchWidgetPage extends SearchWidgetData {

	private static final String[] colours = new String[] { "#000000",
			"#2F4F4F", "#708090", "#778899", "#696969", "#808080", "#A9A9A9",
			"#C0C0C0", "#D3D3D3", "#DCDCDC", "#191970", "#000080", "#00008B",
			"#0000CD", "#0000FF", "#4169E1", "#7B68EE", "#6495ED", "#1E90FF",
			"#00BFFF", "#87CEFA", "#87CEEB", "#ADD8E6", "#B0E0E6", "#B0C4DE",
			"#4682B4", "#5F9EA0", "#00CED1", "#48D1CC", "#40E0D0", "#7FFFD4",
			"#AFEEEE", "#E0FFFF", "#00FFFF", "#00FFFF", "#008080", "#008B8B",
			"#20B2AA", "#8FBC8F", "#66CDAA", "#556B2F", "#808000", "#6B8E23",
			"#9ACD32", "#006400", "#008000", "#228B22", "#2E8B57", "#3CB371",
			"#00FF7F", "#00FA9A", "#90EE90", "#98FB98", "#32CD32", "#00FF00",
			"#7CFC00", "#7FFF00", "#ADFF2F", "#7B68EE", "#483D8B", "#6A5ACD",
			"#4B0082", "#800080", "#8B008B", "#9932CC", "#9400D3", "#8A2BE2",
			"#9966CC", "#9370DB", "#BA55D3", "#FF00FF", "#FF00FF", "#DA70D6",
			"#EE82EE", "#DDA0DD", "#D8BFD8", "#E6E6FA", "#FF69B4", "#FF1493",
			"#C71585", "#DB7093", "#FFA07A", "#FF7F50", "#800000", "#A52A2A",
			"#A0522D", "#8B4513", "#D2691E", "#CD853F", "#B8860B", "#CD5C5C",
			"#F08080", "#FA8072", "#E9967A", "#FFA07A", "#DC143C", "#FF0000",
			"#B22222", "#8B0000", "#FFC0CB", "#FFB6C1", "#FF6347", "#FF4500",
			"#FF8C00", "#FFA500", "#FFD700", "#FFFF00", "#FFFFE0", "#FFFACD",
			"#FAFAD2", "#FFEFD5", "#FFE4B5", "#FFDAB9", "#EEE8AA", "#F0E68C",
			"#BDB76B", "#DAA520", "#F4A460", "#BC8F8F", "#D2B48C", "#DEB887",
			"#F5DEB3", "#FFDEAD", "#FFE4C4", "#FFEBCD", "#FFF8DC", "#FFF5EE",
			"#F5F5DC", "#FDF5E6", "#FFFAF0", "#FFFFF0", "#FAEBD7", "#FAF0E6",
			"#FFF0F5", "#FFE4E1", "#F5F5F5", "#F8F8FF", "#F0F8FF", "#F0FFFF",
			"#F5FFFA", "#F0FFF0", "#FFFAFA", "#FFFFFF", "#FFFFFF", };

	private static final String[] banners = new String[] {
		"poweredbyeuropeanaBlack.png", "poweredbyeuropeanaWhite.png"
	};

	public String[] getColourList() {
		return colours;
	}

	public List<String> getMediaTypes() {
		return ResultTypeTab.getResultTabTypes();
	}

	public String[] getBanners() {
		return banners;
	}

}
