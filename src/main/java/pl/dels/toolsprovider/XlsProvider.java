package pl.dels.toolsprovider;

import java.io.FileOutputStream;
import java.io.IOException;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import pl.dels.model.Activity;
import pl.dels.service.ActivityService;

@AllArgsConstructor
@Service
public class XlsProvider {

	@Autowired
	private ActivityService activityService;

	// method that generates excel file from passed data
	public void generateExcelFile(String path) throws IOException {

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("RAPORT");
		Object[][] datatypes = {
				{ "Nr linii", "ZR", "STRONA", "Czynność", "Uwagi", "Data od", "Data do", "Czas", "Inżynier AOI" } };

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

		List<Activity> activities = activityService
				.getAllActivities((wo1, wo2) -> wo2.getWorkOrder().compareTo(wo1.getWorkOrder()));

		int rowNum2 = 1;

		for (Activity objects : activities) {

			Row row = sheet.createRow(rowNum2++);

			row.createCell(0).setCellValue(objects.getMachineNumber());
			row.createCell(1).setCellValue(objects.getWorkOrder());
			row.createCell(2).setCellValue(objects.getSide());
			row.createCell(3).setCellValue(objects.getActivityType());
			row.createCell(4).setCellValue(objects.getComments());
			row.createCell(5).setCellValue(objects.getStartDateTime());
			row.createCell(6).setCellValue(objects.getStopDateTime());
			row.createCell(7).setCellValue(objects.getDowntime());
			row.createCell(8).setCellValue(objects.getUser().getUsername());
		}

		for (int i = 0; i < 9; i++) {

			sheet.autoSizeColumn(i);
		}

		FileOutputStream outputStream = new FileOutputStream(path);
		workbook.write(outputStream);
		workbook.close();
	}
}