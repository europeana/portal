package eu.europeana.portal2.web.presentation.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.portal2.web.presentation.model.submodel.SortableImage;

public class ImageSorterTest {

	List<SortableImage> images;

	@Before
	public void setUp() throws Exception {
		images = new ArrayList<SortableImage>();
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000001.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000002.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000001.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000003.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000002.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000004.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000003.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000005.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000004.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000006.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000005.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000007.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000006.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000008.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000007.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000009.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000008.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000010.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000009.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000011.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000010.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000012.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000011.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000013.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000012.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000014.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000013.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000015.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000014.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000016.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000015.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000017.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000016.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000018.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000017.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000019.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000018.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000020.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000019.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000021.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000020.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000022.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000021.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000023.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000022.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000024.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000023.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000025.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000024.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000026.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000025.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000027.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000026.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000028.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000027.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000029.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000028.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000030.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000029.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000031.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000030.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000032.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000031.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000033.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000032.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000034.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000033.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000035.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000034.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000036.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000035.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000037.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000036.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000038.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000037.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000039.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000038.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000040.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000039.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000041.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000040.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000042.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000041.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000043.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000042.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000044.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000043.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000045.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000044.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000046.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000045.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000047.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000046.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000048.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000047.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000049.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000048.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000050.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000049.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000051.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000050.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000052.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000051.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000053.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000052.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000054.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000053.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000055.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000054.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000056.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000055.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000057.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000056.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000058.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000057.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000059.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000058.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000060.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000059.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000061.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000060.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000062.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000061.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000063.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000062.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000064.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000063.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000065.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000064.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000066.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000065.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000067.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000066.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000068.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000067.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000069.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000068.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000070.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000069.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000071.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000070.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000072.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000071.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000073.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000072.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000074.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000073.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000075.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000074.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000076.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000075.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000077.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000076.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000078.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000077.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000079.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000078.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000080.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000079.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000081.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000080.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000082.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000081.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000083.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000082.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000084.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000083.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000085.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000084.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000086.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000085.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000087.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000086.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000088.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000087.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000089.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000088.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000090.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000089.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000091.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000090.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000092.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000091.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000093.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000092.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000094.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000093.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000095.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000094.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000096.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000095.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000097.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000096.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000098.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000097.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000099.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000098.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000100.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000099.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000101.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000100.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000102.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000101.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000103.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000102.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000104.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000103.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000105.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000104.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000106.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000105.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000107.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000106.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000108.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000107.jpg"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000109.jpg",
				"http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000108.jpg"));
		images.add(new Image("http://resolver.staatsbibliothek-berlin.de/SBB0000943500000000"));
		images.add(new Image("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/150/0/00000001.png"));
	}

	@Test
	public void testSort() {
		ImageSorter sorter = new ImageSorter(images);
		List<SortableImage> sortedImages = sorter.sort();
		assertEquals(images.size(), sortedImages.size());
		assertNull(sortedImages.get(0).getIsNextInSequence());
		assertNull(sortedImages.get(1).getIsNextInSequence());
		assertNull(sortedImages.get(2).getIsNextInSequence());
		assertNotNull(sortedImages.get(3).getIsNextInSequence());
		assertEquals(images.get(0), sortedImages.get(2));

		int last = sortedImages.size() - 1;
		assertNotNull(sortedImages.get(last).getIsNextInSequence());
		assertEquals("http://content.staatsbibliothek-berlin.de/dms/PPN719131499/1200/0/00000109.jpg",
				sortedImages.get(last).getAbout());
	}

	class Image implements SortableImage {

		String about;
		String isNextInSequence;

		public Image(String about) {
			this.about = about;
		}

		public Image(String about, String isNextInSequence) {
			this(about);
			this.isNextInSequence = isNextInSequence;
		}

		@Override
		public String getAbout() {
			return about;
		}

		@Override
		public String getIsNextInSequence() {
			return isNextInSequence;
		}
	}

}
