package pl.dels.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import pl.dels.toolsprovider.XlsProvider;

@Controller
public class UserController {

	private XlsProvider xlsProvider;

	@Autowired
	public void setXlsProvider(XlsProvider xlsProvider) {
		this.xlsProvider = xlsProvider;
	}

	@GetMapping("/login")
	private String login() throws IOException {

		xlsProvider.generateExcelFile();

		return "login";
	}

	@GetMapping("/logout")
	private String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}
}
