package pl.coderslab.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.coderslab.entity.Client;
import pl.coderslab.service.CsvImportExportService;

@Controller
@RequestMapping("/import")
public class ImportController {

	@Autowired
	private CsvImportExportService csvService;

	/**Show input for filename to import
	 * 
	 * @return form
	 */
	@GetMapping
	public String importCsvView() {
		return "import/getFilename";
	}

	/**Import given file
	 * 
	 * @param filename without path or .csv
	 * @param model
	 * @return view with result and list of imported clients
	 */
	@PostMapping
	public String importCsv(@RequestParam String filename, Model model) {
		String newFilename = "csvImport/" + filename;

		List<Client> clientList = new ArrayList<>();

		String result = "File imported";
		try {
			clientList = csvService.readCsvWithHeader(newFilename);
		} catch (IOException e) {
			result = "Error occured while importing file";
			e.printStackTrace();
		}

		model.addAttribute("clientList", clientList);
		model.addAttribute("result", result);

		return "import/show";
	}
}
