package pl.dels.toolsprovider;

import java.io.FileOutputStream;
import java.io.IOException;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import com.gembox.spreadsheet.*;
import com.gembox.spreadsheet.charts.*;

import pl.dels.database.dao.ChartActivityDaoImpl;
import pl.dels.model.Activity;
import pl.dels.model.ChartActivity;
import pl.dels.service.ActivityService;

@AllArgsConstructor
@Service
public class XlsProvider {

	@Autowired
	private ActivityService activityService;

	// method that generates excel file
	public void generateExcelFileWithAllDataFromDb(String path) throws IOException {

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

		XSSFCellStyle cellStyle = workbook.createCellStyle();
		CreationHelper createHelper = workbook.getCreationHelper();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));

		List<Activity> activities = activityService
				.getAllActivities((wo1, wo2) -> wo2.getWorkOrder().compareTo(wo1.getWorkOrder()));

		int rowNum2 = 1;

		for (Activity activity : activities) {

			Row row = sheet.createRow(rowNum2++);

			row.createCell(0).setCellValue(String.valueOf(activity.getMachineNumber()));
			row.createCell(1).setCellValue(activity.getWorkOrder());
			row.createCell(2).setCellValue(String.valueOf(activity.getSide()));
			row.createCell(3).setCellValue(String.valueOf(activity.getActivityType()));
			row.createCell(4).setCellValue(activity.getComments());
			Cell startDateCell = row.createCell(5);
			startDateCell.setCellValue(activity.getStartDateTime());
			startDateCell.setCellStyle(cellStyle);
			Cell stopDateCell = row.createCell(6);
			stopDateCell.setCellValue(activity.getStopDateTime());
			stopDateCell.setCellStyle(cellStyle);
			row.createCell(7).setCellValue(activity.getDowntime());
			row.createCell(8).setCellValue(activity.getUser().getUsername());
		}

		for (int i = 0; i < 9; i++) {

			sheet.autoSizeColumn(i);
		}

		FileOutputStream outputStream = new FileOutputStream(path);
		workbook.write(outputStream);
		workbook.close();
	}

	public void generateExcelFileWithChartFromAGivenWeek() throws IOException {

		SpreadsheetInfo.setLicense("FREE-LIMITED-KEY");

		ExcelFile workbook = new ExcelFile();
		ExcelWorksheet worksheet = workbook.addWorksheet("Porownanie");

		List<Activity> aoiActivities = activityService
				.getAllActivities((wo1, wo2) -> wo2.getWorkOrder().compareTo(wo1.getWorkOrder()));

		List<ChartActivity> chartActivityAoiList = aoiActivities.stream()
													.map(activity -> new ChartActivity(activity.getWorkOrder(),aoiActivities.stream()
															.filter(activityWO -> activityWO.getWorkOrder().equals(activity.getWorkOrder()))
															.mapToDouble(activityWO -> activityWO.getDowntime()).sum()))
													.distinct().collect(Collectors.toList());

		ChartActivityDaoImpl chartActivityDao = new ChartActivityDaoImpl();

		List<ChartActivity> kronosActivities = chartActivityDao.getAllActivities();

		List<ChartActivity> chartActivityKronosList = kronosActivities.stream()
														.map(activity -> new ChartActivity(activity.getWorkOrder(),kronosActivities.stream()
																.filter(activityWO -> activityWO.getWorkOrder().equals(activity.getWorkOrder()))
																.mapToDouble(activityWO -> activityWO.getDowntime()).sum()))
														.distinct().collect(Collectors.toList());

		ExcelChart chart = worksheet.getCharts().add(ChartType.BAR, "F2", "M25");
		chart.selectData(worksheet.getCells().getSubrangeAbsolute(0, 0, chartActivityAoiList.size(), 2), true);

		for (int i = 0; i < chartActivityAoiList.size(); i++) {
			worksheet.getCell(i + 1, 0).setValue(chartActivityAoiList.get(i).getWorkOrder());
			worksheet.getCell(i + 1, 1).setValue(chartActivityAoiList.get(i).getDowntime());
			worksheet.getCell(i + 1, 2).setValue(chartActivityKronosList.get(i).getDowntime());
		}
		worksheet.getCell(0, 0).setValue("ZR");
		worksheet.getCell(0, 1).setValue("Downtime_AR");
		worksheet.getCell(0, 2).setValue("Downtime_Kronos");

		worksheet.getCell(0, 0).getStyle().getFont().setWeight(ExcelFont.BOLD_WEIGHT);
		worksheet.getCell(0, 1).getStyle().getFont().setWeight(ExcelFont.BOLD_WEIGHT);
		worksheet.getCell(0, 2).getStyle().getFont().setWeight(ExcelFont.BOLD_WEIGHT);

		int columnCount = worksheet.calculateMaxUsedColumns();

		for (int i = 0; i < columnCount; i++)
			worksheet.getColumn(i).setWidth((int) LengthUnitConverter.convert(3, LengthUnit.CENTIMETER,
					LengthUnit.ZERO_CHARACTER_WIDTH_256_TH_PART));

		workbook.save("C:\\Users\\danelczykl\\Desktop\\Raport_porownanie_.xlsx");
	}
}