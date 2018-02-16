package pl.coderslab.web;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;

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
import pl.coderslab.entity.User;
import pl.coderslab.service.ClientService;
import pl.coderslab.service.ContractService;
import pl.coderslab.service.UserService;

@Controller
@RequestMapping("/contract")
public class ContractController {

	@Autowired
	private UserService userService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private ContractService contractService;
	@Autowired
	private AuthenticationFacade authenticationFacade;

	// FORM NEW CONTRACT
	@GetMapping("/new/{id}")
	public String newContract(@PathVariable Long id, Model model) {
		Contract contract = new Contract();
		model.addAttribute("contract", contract);
		model.addAttribute("id", id);

		return "contract/new";
	}

	// GENERATING NEW CONTRACT
	@PostMapping("/new/{id}")
	public String registerUser(@PathVariable Long id, @ModelAttribute("contract") @Valid Contract contract,
			BindingResult bresult, Model model) {
		if (bresult.hasErrors()) {
			return "contract/new";
		} else {
			String result;
			User user = authenticationFacade.getAuthenticatedUser();
			Client client = clientService.findById(id);
			contract.setId(null);
			contract.setClient(client);
			contract.setAuthor(user);
			contract.setCreated(LocalDateTime.now());
			contract.setFilename("contracts/"+contract.getFilename());
			

			if (contract.getValue() < userService.getMaxContractValue(user)) {
				contract.setAcceptedBy(user);
				contract.setAccepted(true);
				contractService.save(contract);
				result = "Contract generated";
			} else if (contractService.sendToSupervisor(contract, user)) {
				result = "Contract send to supervisor";		
			} else {
				result = "You don't have a supervisor who can accept this contract";
			}
			model.addAttribute("result", result);
			return "contract/result";
		}
	}

	// ACCEPTING CONTRACT
	@GetMapping("/accept/{id}")
	public String acceptContract(@PathVariable Long id, Model model) {
		Contract contract = contractService.findById(id);
		User user = authenticationFacade.getAuthenticatedUser();
		String result;
		if (contract.getAcceptedBy().equals(user)) {
			contractService.acceptContract(contract);
			result = "Contract accepted";
		} else {
			result = "You can't accept this contract";
		}
		model.addAttribute("result", result);
		return "contract/result";
	}

	// PRINTING CONTRACT
	@GetMapping("/print/{id}")
	public String printContract(@PathVariable Long id, Model model) {
		Contract contract = contractService.findById(id);
		User user = authenticationFacade.getAuthenticatedUser();
		String result;
		if (contract.isAccepted()) {
			try {
				contractService.print(contract);
				result = "Contract printed";
			} catch (FileNotFoundException e) {
				result = "Contract not printed. File not found.";
				e.printStackTrace();
			}
		} else {
			result = "Contract must be accepted first";
		}
		
		model.addAttribute("result", result);
		return "contract/result";
	}

}
