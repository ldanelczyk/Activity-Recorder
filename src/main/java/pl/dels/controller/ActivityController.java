package pl.dels.controller;

import java.io.IOException;
import java.sql.Timestamp;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.dels.model.Activity;
import pl.dels.model.enums.MachineNumber;
import pl.dels.model.enums.Side;
import pl.dels.service.ActivityService;
import pl.dels.toolsprovider.XlsProvider;

@Controller
public class ActivityController {

	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private XlsProvider xlsProvider;
	
	private Timestamp startDateTime;

	@PostMapping("/startRegistration")
	private String startRegistration(@RequestParam MachineNumber machineNumber, @RequestParam String workOrder,
			@RequestParam Side side, @RequestParam String activityType, Model model) {

		startDateTime = new Timestamp(new Date().getTime());

		Activity activity = activityService.createTempActivity(machineNumber, workOrder, side, activityType);

		model.addAttribute("activity", activity);

		return "started";
	}

	@PostMapping("/stopRegistration")
	private String stopRegistration(@RequestParam MachineNumber machineNumber, @RequestParam String workOrder,
			@RequestParam Side side, @RequestParam String activityType, @RequestParam String comments) {

		String nameOfLoggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
		
		//String nameOfLoggedUser = "ExampleUser";

		Timestamp stopDateTime = new Timestamp(new Date().getTime());

		double downtime = activityService.downtimeCounter(startDateTime, stopDateTime);
		
		activityService.saveActivityInDatabase(machineNumber, workOrder, side, activityType, comments,
				startDateTime, stopDateTime, downtime, nameOfLoggedUser);
		
		try {
			
			xlsProvider.generateExcelFileWithAllDataFromDb("C:\\Users\\danelczykl\\Desktop\\Raport_czynnosci.xlsx");
			xlsProvider.generateExcelFileWithChartFromAGivenWeek();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		return "redirect:addOk";
	}

	@GetMapping("/addOk")
	private String addActivity() {

		return "addSuccess";
	}
}
