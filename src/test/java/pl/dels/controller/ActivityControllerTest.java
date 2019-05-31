package pl.dels.controller;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import static org.hamcrest.Matchers.hasProperty;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;
import java.util.Date;

import pl.dels.model.Activity;
import pl.dels.service.ActivityService;

class ActivityControllerTest {

	@InjectMocks
	private ActivityController activityController;

	private MockMvc mockMvc;

	@Mock
	ActivityService activityService; 
	
	private Timestamp startDateTime = new Timestamp(new Date().getTime());

	private Timestamp stopDateTime = new Timestamp(new Date().getTime());

	@Test
	public void startRegistration_shouldReturnStartedPageAndAddTempActivityAsAttribute() throws Exception {

		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(activityController).build();

		Activity createdActivity = createTempActivity();

		String machineNumber = "AOI1";
		String workOrder = "ZRXXXX";
		String side = "TOP";
		String activityType = "Pisanie programu AOI";

		when(activityService.createTempActivity(machineNumber, workOrder, side, activityType)).thenReturn(createdActivity);

		this.mockMvc.perform(post("/startRegistration").contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.param("machineNumber", machineNumber)
					.param("workOrder", workOrder)
					.param("side", side)
					.param("activityType", activityType))
					.andExpect(status().isOk())
					.andExpect(view().name("started"))
					.andExpect(forwardedUrl("started"))
					.andExpect(model().attribute("activity", hasProperty("machineNumber", is(machineNumber))))
					.andExpect(model().attribute("activity", hasProperty("workOrder", is(workOrder))))
					.andExpect(model().attribute("activity", hasProperty("side", is(side))))
					.andExpect(model().attribute("activity", hasProperty("activityType", is(activityType))));

		verify(activityService, times(1)).createTempActivity(machineNumber, workOrder, side, activityType);
		verifyNoMoreInteractions(activityService);
	}

	@Test
	public void stopRegistration_shouldReturnStartedPageAndAddTempActivityAsAttribute() throws Exception {

		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(activityController).build();
		
		Activity createdActivity = createActivity();
		
		String machineNumber = "AOI1";
		String workOrder = "ZRXXXX";
		String side = "TOP";
		String activityType = "Pisanie programu AOI";
		String comments = "Example Comment2";
		
		when(activityService.saveActivityInDatabase("AOI2", "ZRXXYY", "BOT", "Poprawa programu AOI", "Example Comment2", startDateTime, stopDateTime, 0.001, null)).thenReturn(createdActivity);
				
		//when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("exampleName");

		this.mockMvc.perform(post("/stopRegistration").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("machineNumber", machineNumber)
				.param("workOrder", workOrder)
				.param("side", side)
				.param("activityType", activityType)
				.param("comments", comments))
				.andExpect(view().name("redirect:addOk"));
	}
	
	private Activity createActivity() {
		
		Activity activity = Activity.builder()
				.machineNumber("AOI2")
				.workOrder("ZRXXYY")
				.side("BOT")
				.activityType("Poprawa programu AOI")
				.comments("Example Comment2")
				.startDateTime(startDateTime)
				.stopDateTime(stopDateTime)
				.downtime(0.001)
				.build();
		
		return activity;
	}

	Activity createTempActivity() {

		Activity activity = Activity.builder()
				.machineNumber("AOI1")
				.workOrder("ZRXXXX")
				.side("TOP")
				.activityType("Pisanie programu AOI").build();
		
		return activity;
	}
}
