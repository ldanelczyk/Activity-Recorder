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
import lombok.NoArgsConstructor;

import com.gembox.spreadsheet.*;
import com.gembox.spreadsheet.charts.*;

import pl.dels.model.Activity;
import pl.dels.model.ChartActivity;
import pl.dels.service.ActivityService;
import pl.dels.service.ChartActivityService;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class XlsProvider {

	@Autowired
	private ActivityService activityService;

	@Autowired
	private ChartActivityService chartActivityService;

	public void generateExcelFileWithAllDataFromDb(String path) throws IOException {

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

		XSSFCellStyle cellStyle = workbook.createCellStyle();
		CreationHelper createHelper = workbook.getCreationHelper();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));

		List<Activity> activities = activityService
				.getAllActivitiesFromMySql((wo1, wo2) -> wo2.getWorkOrder().compareTo(wo1.getWorkOrder()));

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

	public void generateExcelFileWithChartFromAGivenWeek(String path) throws IOException, ClassNotFoundException {

		SpreadsheetInfo.setLicense("FREE-LIMITED-KEY");

		ExcelFile workbook = new ExcelFile();
		ExcelWorksheet worksheet = workbook.addWorksheet("Porownanie");

		List<ChartActivity> chartActivityAoiList = activityService.getProcessedChartActivitiesFromMySql();
		
		// Weryfikacja czynności AR
				System.out.println();
				System.out.println("Czynności AR po przetworzeniu:");
				chartActivityAoiList.forEach(System.out::println);
		
		List<ChartActivity> chartActivityKronosList = chartActivityService.getProcessedChartActivitiesFromFirebird();
		
		// Weryfikacja czynności Kronos
				System.out.println();
				System.out.println("Czynności KRONOS po filtrowaniu i przetworzeniu:");
				chartActivityKronosList.forEach(System.out::println);
				
		List<ChartActivity> chartActivityAoiFilteredList = chartActivityAoiList.stream()
				.filter(aoiActivity -> chartActivityKronosList.stream().anyMatch(kronosActivity -> kronosActivity.getWorkOrder().equals(aoiActivity.getWorkOrder())))
				.collect(Collectors.toList());
		
		// Weryfikacja czynności AR 2
		System.out.println();
		System.out.println("Czynności AR po filtrowaniu:");
		chartActivityAoiFilteredList.forEach(System.out::println);
		
		ColumnChart chart = (ColumnChart) worksheet.getCharts().add(ChartType.COLUMN, "F2", "M25");
		chart.selectData(worksheet.getCells().getSubrangeAbsolute(0, 0, chartActivityAoiFilteredList.size(), 2), true);

		chart.getTitle().setText("Czas czynności AOI");
		chart.getAxes().getHorizontal().getTitle().setText("Numer ZR");
		chart.getAxes().getVertical().getTitle().setText("Czas czynności AOI [h]");

		for (int i = 0; i < chartActivityKronosList.size(); i++) {
			worksheet.getCell(i + 1, 0).setValue(chartActivityAoiFilteredList.get(i).getWorkOrder());
			worksheet.getCell(i + 1, 1).setValue(chartActivityAoiFilteredList.get(i).getDowntime());
			worksheet.getCell(i + 1, 2).setValue(MathToolsProvider.round(chartActivityKronosList.get(i).getDowntime(), 3));
		}

		worksheet.getCell(0, 0).setValue("ZR");
		worksheet.getCell(0, 1).setValue("Downtime_DPP");
		worksheet.getCell(0, 2).setValue("Downtime_Kronos");

		worksheet.getCell(0, 0).getStyle().getFont().setWeight(ExcelFont.BOLD_WEIGHT);
		worksheet.getCell(0, 1).getStyle().getFont().setWeight(ExcelFont.BOLD_WEIGHT);
		worksheet.getCell(0, 2).getStyle().getFont().setWeight(ExcelFont.BOLD_WEIGHT);

		int columnCount = worksheet.calculateMaxUsedColumns();

		for (int i = 0; i < columnCount; i++)
			worksheet.getColumn(i).setWidth((int) LengthUnitConverter.convert(3, LengthUnit.CENTIMETER, LengthUnit.ZERO_CHARACTER_WIDTH_256_TH_PART));

		workbook.save(path);
	}

	public XlsProvider(ActivityService activityService) {
		super();
		this.activityService = activityService;
	}
}