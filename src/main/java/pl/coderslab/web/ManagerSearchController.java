package pl.coderslab.web;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import pl.coderslab.AuthenticationFacade;
import pl.coderslab.entity.Client;
import pl.coderslab.entity.User;
import pl.coderslab.repository.ClientRepository;
import pl.coderslab.service.AddressService;
import pl.coderslab.service.ClientService;
import pl.coderslab.service.CsvImportExportService;
import pl.coderslab.service.PdfService;
import pl.coderslab.service.UserService;

@Controller
@RequestMapping("/managerSearch")
public class ManagerSearchController {

	private List<Client> clientList;
	private String search;

	@Autowired
	private AuthenticationFacade authenticationFacade;
	@Autowired
	private ClientService clientService;
	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private AddressService addressService;
	@Autowired
	private PdfService pdfService;
	@Autowired
	private CsvImportExportService csvService;

	@ModelAttribute
	public void addAttributes(Model model) {
		model.addAttribute("statusList", clientService.getStatusList());
		model.addAttribute("cityList", addressService.getCitiesList());
	}

	// DEFAULT SEARCH - TEAM CLIENTS SEARCH
	@GetMapping
	public String defaultSearch(Model model) {
		User user = authenticationFacade.getAuthenticatedUser();
		List<User> employees = userService.findBySupervisor(user);
		clientList = clientRepository.findByUserIn(employees);
		model.addAttribute("clientList", clientList);
		return "manager/search";
	}

	// ALL CLIENTS SEARCH
	@GetMapping("/all")
	public String allSearch(Model model) {
		clientList = clientRepository.findAll();
		model.addAttribute("clientList", clientList);
		return "manager/search";
	}

	// CITY SEARCH
	@PostMapping(path = "/city")
	public String citySearch(@RequestParam String city, Model model) {
		clientList = clientRepository.findByAddressCityContainingIgnoreCaseOrderByNameAsc(city);
		model.addAttribute("clientList", clientList);
		return "manager/search";
	}

	// MANAGER REGION SEARCH
	@GetMapping(path = "/region")
	public String citySearch(Model model) {
		User user = authenticationFacade.getAuthenticatedUser();
		clientList = clientRepository
				.findByAddressRegionOrderByNameAsc(user.getOffice().getAddress().getRegion());
		model.addAttribute("clientList", clientList);
		return "manager/search";
	}

	// STATUS SEARCH
	@PostMapping(path = "/status")
	public String statusSearch(@RequestParam String status, Model model) {
		clientList = clientRepository.findByStatusOrderByNameAsc(status);
		model.addAttribute("clientList", clientList);
		return "manager/search";
	}

	// NAME SEARCH
	@PostMapping(path = "/name")
	public String nameSearch(@RequestParam String name, Model model) {
		clientList = clientRepository.findByNameContainingIgnoreCaseOrderByNameAsc(name);
		model.addAttribute("clientList", clientList);
		return "manager/search";
	}

	// PRINT SEARCHED LIST OF CLIENTS INTO PDF FILE
	@GetMapping(path = "/print")
	public String printSearch(Model model) {
		User user = authenticationFacade.getAuthenticatedUser();
		String filename = "pdf/" + user.getLastname() + "_" + search + "_" + LocalDateTime.now();
		String result = pdfService.printClientList(filename, clientList);
		model.addAttribute("result", result);
		return "manager/result";
	}

	// EXPORT SEARCHED LIST OF CLIENTS INTO CSV FILE
	@PostMapping(path = "/export")
	public String exportSearch(@RequestParam String filename, Model model) {
		User user = authenticationFacade.getAuthenticatedUser();

		String newFilename = "csvExport/" + filename + "_" + user.getLastname();
		String result = "File " + newFilename + ".csv generated correctly";

		try {
			csvService.writeCsv(newFilename, clientList);

		} catch (CsvDataTypeMismatchException e) {
			result = "Document not generated. Data type not matching";
			e.printStackTrace();
		} catch (CsvRequiredFieldEmptyException e) {
			result = "Document not generated. Required field is empty";
			e.printStackTrace();
		} catch (IOException e) {
			result = "Document not generated. Error occured.";
			e.printStackTrace();
		}
		model.addAttribute("result", result);
		return "manager/result";
	}
	
	

}
