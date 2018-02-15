package pl.coderslab.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import pl.coderslab.AuthenticationFacade;
import pl.coderslab.entity.Contract;
import pl.coderslab.entity.Notification;
import pl.coderslab.entity.User;
import pl.coderslab.repository.NotificationRepository;
import pl.coderslab.service.ContractService;

@Controller
public class HomeController {
	
	
	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private ContractService contractService;
	@Autowired
	private AuthenticationFacade authenticationFacade;

	//SHOW HOME PAGE FOR LOGGED USER
	@GetMapping("/")
    public String home(Model model) {
		User user = authenticationFacade.getAuthenticatedUser();
		model.addAttribute("user", user);
		
		List<Notification> notificationList = notificationRepository.findByUser(user);
		model.addAttribute("notificationList", notificationList);
		
		List<Contract> contractList = contractService.findByAcceptedBy(user);
		model.addAttribute("contractList", contractList);
		
        return "index";
	}
	
}
