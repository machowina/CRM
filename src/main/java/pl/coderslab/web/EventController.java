package pl.coderslab.web;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.coderslab.AuthenticationFacade;
import pl.coderslab.entity.Client;
import pl.coderslab.entity.Contract;
import pl.coderslab.entity.Event;
import pl.coderslab.entity.User;
import pl.coderslab.repository.EventRepository;
import pl.coderslab.service.ClientService;

@Controller
@RequestMapping("/event")
public class EventController {

	@Autowired
	private AuthenticationFacade authenticationFacade;
	@Autowired
	private ClientService clientService;
	@Autowired
	private EventRepository eventRepository;

	// ADDING NEW EVENT
	@GetMapping("/new")
	public String newEvent(Model model) {

		Event event = new Event();
		model.addAttribute("event", event);

		User user = authenticationFacade.getAuthenticatedUser();
		List<Client> clientList = clientService.findByUser(user);
		model.addAttribute("clientList", clientList);

		return "event/new";
	}

	// GENERATING EVENT
	@PostMapping("/new")
	public String registerUser(@ModelAttribute("event") @Valid Event event,
			BindingResult bresult, Model model) {
		System.out.println("aaa");
		if (bresult.hasErrors()) {
			return "event/new";
		} else {
			System.out.println(event.getTitle());
			System.out.println(event.getTime());
			String result = "";
			User user = authenticationFacade.getAuthenticatedUser();
			event.setUser(user);
			
			System.out.println(event.getClient().getName());
			//eventRepository.save(event);
			
			model.addAttribute("result", result);
			return "event/result";
		}
	}

}
