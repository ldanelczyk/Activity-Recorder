package pl.dels.controller;

import java.io.IOException;

import java.sql.Timestamp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Timer;

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
import pl.dels.toolsprovider.ScheduledTask;
import pl.dels.toolsprovider.TimeToolsProvider;
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
			@RequestParam Side side, @RequestParam String activityType, Model model)
			throws ParseException, ClassNotFoundException, IOException {

		startDateTime = new Timestamp(new Date().getTime());

		Activity activity = activityService.createTempActivity(machineNumber, workOrder, side, activityType);

		model.addAttribute("activity", activity);

		Timer timer = new Timer();
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = dateFormatter.parse("2019-06-17 22:55:00");
		timer.schedule(new ScheduledTask(xlsProvider), date, 10000);

		return "started";
	}

	@PostMapping("/stopRegistration")
	private String stopRegistration(@RequestParam MachineNumber machineNumber, @RequestParam String workOrder,
			@RequestParam Side side, @RequestParam String activityType, @RequestParam String comments)
			throws ClassNotFoundException {

		String nameOfLoggedUser = SecurityContextHolder.getContext().getAuthentication().getName();

		//TEMP ref for test
		//String nameOfLoggedUser = "ExampleUser";

		Timestamp stopDateTime = new Timestamp(new Date().getTime());

		double downtime = TimeToolsProvider.downtimeCounter(startDateTime, stopDateTime);

		activityService.saveActivityInDatabase(machineNumber, workOrder, side, activityType, comments, startDateTime,
				stopDateTime, downtime, nameOfLoggedUser);

		try {

			xlsProvider.generateExcelFileWithAllDataFromDb("C:\\Users\\danelczykl\\Desktop\\Raport_czynnosci.xlsx");

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
