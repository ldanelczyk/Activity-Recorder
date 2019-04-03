package pl.dels.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.dels.service.ActivityRecorderService;

@Controller
public class ActivityController {

	private ActivityRecorderService activityRecorderService;

	@Autowired
	private void setPostService(ActivityRecorderService activityRecorderService) {
		this.activityRecorderService = activityRecorderService;
	}

	@RequestMapping(value = "/startRegistration", method = RequestMethod.GET)
	private String startRegistration() {

		return "started";
	}

	/*@PostMapping("/stopRegistration")
	private void stopRegistration() {

		System.out.println("stopped");
	}

	@PostMapping("/addActivity")
	private String addActivity(@RequestParam String machineNumber, @RequestParam String workOrder,
			@RequestParam String side, @RequestParam String activityType, @RequestParam String comments) {

		String nameOfLoggedUser = SecurityContextHolder.getContext().getAuthentication().getName();

		return "redirect:addOk";

	}*/
}
