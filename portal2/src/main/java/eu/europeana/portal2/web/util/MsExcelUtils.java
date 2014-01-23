package eu.europeana.portal2.web.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class MsExcelUtils {

	public static void build(
			String title,
			List<Map<String, String>> headerData,
			List<List<String>> revenueData,
			HSSFWorkbook workbook)
		throws Exception {

		HSSFSheet sheet = workbook.createSheet(title);

		HSSFRow header = sheet.createRow(0);
		int cellNum = 0;
		for (Map<String, String> cell : headerData) {
			header.createCell(cellNum++).setCellValue(cell.get("name"));
		}

		int rowNum = 1;
		for (List<String> cells : revenueData) {
			HSSFRow row = sheet.createRow(rowNum++);
			cellNum = 0;
			for (String cell : cells) {
				String type = headerData.get(cellNum).containsKey("type") ? headerData.get(cellNum).get("type") : "String";
				if (StringUtils.equals(type, "int")) {
					row.createCell(cellNum++).setCellValue(Double.parseDouble(cell));
				} else {
					row.createCell(cellNum++).setCellValue(cell);
				}
			}
		}
	}

	public static void flush(HttpServletResponse response, String filename, HSSFWorkbook workbook) {
		response.setContentType("application/vnd.ms-excel; charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".xls");

		try {
			OutputStream outputStream = response.getOutputStream();
			workbook.write(outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
