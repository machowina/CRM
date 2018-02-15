package pl.coderslab.web;

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

import pl.coderslab.entity.Client;
import pl.coderslab.entity.Contract;
import pl.coderslab.entity.Event;
import pl.coderslab.repository.EventRepository;
import pl.coderslab.service.ClientService;
import pl.coderslab.service.ContractService;
import pl.coderslab.service.UserService;

@Controller
@RequestMapping("client")
public class ClientController {

	@Autowired
	private ClientService clientService;
	@Autowired
	private UserService userService;
	@Autowired
	private ContractService contractService;
	@Autowired
	private EventRepository eventRepository;

	// ADDING NEW CLIENT
	@GetMapping(path = "/add")
	public String addClient(Model model) {

		Client client = new Client();
		model.addAttribute("client", client);

		List<String> statusList = clientService.getStatusList();
		model.addAttribute("statusList", statusList);

		return "client/addClient";
	}
	//SAVING NEW CLIENT
	@PostMapping(path = "/add")
	public String registerClient(@ModelAttribute("client") @Valid Client client, BindingResult bresult) {

		if (bresult.hasErrors()) {
			return "client/addClient";
		} else {
			clientService.saveClientWithLoggedUser(client);
			return "client/success";
		}
	}

	// EDITING CLIENT
	@GetMapping(path = "/edit/{id}")
	public String editClient(@PathVariable Long id, Model model) {

		Client client = clientService.findById(id);
		model.addAttribute("client", client);

		List<String> statusList = clientService.getStatusList();
		model.addAttribute("statusList", statusList);

		return "client/editClient";
	}
	//SAVING EDITED CLIENT
	@PostMapping(path = "/edit/{id}")
	public String seveClient(@ModelAttribute("client") @Valid Client client, BindingResult bresult,
			@PathVariable Long id) {

		if (bresult.hasErrors()) {
			return "client/editClient";
		} else {

			client.setId(id);
			client.setUser(userService.findByEmail(client.getUser().getEmail()));
			clientService.saveClient(client);
			return "client/success";
		}
	}

	// CLIENT DETAILS
	@GetMapping(path = "/details/{id}")
	public String clientDetails(@PathVariable Long id, Model model) {

		Client client = clientService.findById(id);
		model.addAttribute("client", client);

		List<Contract> contractList = contractService.findByClient(client);
		model.addAttribute("contractList", contractList);

		List<Event> eventList = eventRepository.findByClient(client);
		model.addAttribute("eventList", eventList);

		return "client/clientDetails";
	}

	// ADDING SIMILAR CLIENT
	@GetMapping(path = "/addSimilar/{id}")
	public String addSimilarClient(@PathVariable Long id, Model model) {

		Client client = clientService.findById(id);
		model.addAttribute("client", client);

		List<String> statusList = clientService.getStatusList();
		model.addAttribute("statusList", statusList);

		return "client/addSimilarClient";
	}

}
