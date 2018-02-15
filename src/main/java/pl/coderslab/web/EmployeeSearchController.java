package pl.coderslab.web;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import pl.coderslab.AuthenticationFacade;
import pl.coderslab.entity.Client;
import pl.coderslab.entity.User;
import pl.coderslab.repository.ClientRepository;
import pl.coderslab.service.ClientService;
import pl.coderslab.service.CsvImportExportService;
import pl.coderslab.service.PdfService;

@Controller
@RequestMapping("/employeeSearch")
public class EmployeeSearchController {

	private List<Client> clientList;
	private String search;

	@Autowired
	private AuthenticationFacade authenticationFacade;
	@Autowired
	private ClientService clientService;
	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private PdfService pdfService;
	@Autowired
	private CsvImportExportService csvService;

	@ModelAttribute
	public void addAttributes(Model model) {
		model.addAttribute("statusList", clientService.getStatusList());
	}

	// DEFAULT SEARCH - USER CLIENTS
	@GetMapping
	public String defaultSearch(Model model) {
		User user = authenticationFacade.getAuthenticatedUser();
		clientList = clientRepository.findByUser(user);
		model.addAttribute("clientList", clientList);
		search = "clients_maneged_by_" + user.getLastname();
		return "employee/search";
	}

	// CITY SEARCH
	@GetMapping(path = "/city")
	public String citySearch(Model model) {
		User user = authenticationFacade.getAuthenticatedUser();
		clientList = clientRepository.findByAddressCityOrderByNameAsc(user.getOffice().getAddress().getCity());
		model.addAttribute("clientList", clientList);
		search = "clients_from_" + user.getOffice().getAddress().getCity();
		return "employee/search";
	}

	// STATUS SEARCH FROM USER CITY
	@PostMapping(path = "/status")
	public String statusSearch(@RequestParam String status, Model model) {
		User user = authenticationFacade.getAuthenticatedUser();
		clientList = clientRepository.findByStatusAndAddressCityOrderByNameAsc(status,
				user.getOffice().getAddress().getCity());
		model.addAttribute("clientList", clientList);
		search = "clients_with_status_" + status;
		return "employee/search";
	}

	// NAME SEARCH FROM USER CITY
	@PostMapping(path = "/name")
	public String nameSearch(@RequestParam String name, Model model) {
		User user = authenticationFacade.getAuthenticatedUser();
		System.out.println(name);
		clientList = clientRepository.findByNameContainingIgnoreCaseAndAddressCityOrderByNameAsc(name,
				user.getOffice().getAddress().getCity());
		model.addAttribute("clientList", clientList);
		search = "clients_with_name_" + name;
		return "employee/search";
	}

	// PRINT SEARCHED CLIENT LIST
	@GetMapping(path = "/print")
	public String printSearch(Model model) {
		User user = authenticationFacade.getAuthenticatedUser();
		String filename = "pdf/" + user.getLastname() + "_" + search + "_" + LocalDateTime.now();
		String result = pdfService.printClientList(filename, clientList);
		model.addAttribute("result", result);
		return "employee/result";
	}

	// EXPORT SEARCHED CLIENT LIST
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
		return "employee/result";
	}

}
