package pl.dels.toolsprovider;

import java.io.IOException;

import java.time.LocalTime;

import java.util.TimerTask;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ScheduledTask extends TimerTask {
	
	XlsProvider xlsProvider;
	
	@Override
	public void run() {
		
		try {
			
			xlsProvider.generateExcelFileWithChartFromAGivenWeek("C:\\Users\\danelczykl\\Desktop\\Raport_porownanie_"+TimeToolsProvider.convertTimeToFileName(LocalTime.now())+".xlsx");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
