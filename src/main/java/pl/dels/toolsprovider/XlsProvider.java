package pl.dels.toolsprovider;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import pl.dels.model.Activity;

public class XlsProvider {

	//method that generates excel file from passed data
	public  static void generateExcelFile() throws IOException {

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("RAPORT");
		Object[][] datatypes = {{"Nr linii", "ZR", "STRONA", "Czynność", "Uwagi", "Data od", "Data do", "Czas", "Inżynier AOI"}};

		int rowNum = 0;

		for (Object[] datatype : datatypes) {

			Row row = sheet.createRow(rowNum++);
			int colNum = 0;
			for (Object field : datatype) {

				Cell cell = row.createCell(colNum++);
				if (field instanceof String) {
					cell.setCellValue((String) field);
				} else if (field instanceof Integer) {
					cell.setCellValue((Integer) field);
				}
			}
		}

		/*int rowNum2 = 19;

		for (Activity objects : listOfActivities) {

			Row row = sheet.createRow(rowNum2++);
			row.createCell(0).setCellValue("");
			row.createCell(1).setCellValue("");
			row.createCell(2).setCellValue("");
			row.createCell(3).setCellValue(objects.getDate());
			row.createCell(4).setCellValue(objects.getAoiNumber());
			row.createCell(5).setCellValue(objects.getLotNumber());
			row.createCell(6).setCellValue(objects.getAoiProgramName());
			row.createCell(7).setCellValue(objects.getPcbSide());
			row.createCell(8).setCellValue(objects.getDefectType());
			row.createCell(9).setCellValue(objects.getDesignator());
			row.createCell(10).setCellValue(objects.getCounterOfDefectType());
			row.createCell(11).setCellValue(objects.getPartNumber());
			row.createCell(12).setCellValue(objects.getPackageType());
		}*/

		for (int i = 0; i < 30; i++) {

			sheet.autoSizeColumn(i);
		}

			FileOutputStream outputStream = new FileOutputStream(
					"C:\\Users\\danelczykl\\Desktop\\test.xlsx");
			workbook.write(outputStream);
			workbook.close();
	}
}