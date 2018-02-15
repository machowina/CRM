package pl.coderslab.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import pl.coderslab.entity.Client;
import pl.coderslab.entity.Company;
import pl.coderslab.entity.Contract;
import pl.coderslab.repository.CompanyRepository;

@Service
public class PdfService {
	
	@Autowired
	CompanyRepository companyRepository;

	private static final String TYPE = ".pdf";
	private static final List<String> TITLE_LIST = Arrays.asList("Id", "Name", "Status", "Created", "Contact name",
			"Contact email", "City", "Region", "Responsible employee");

	/**Sends given Client list to print into pdf file
	 * 
	 * @param filename without .pdf, with folder path
	 * @param clientList to print
	 * @return String result of action
	 */
	public String printClientList(String filename, List<Client> clientList) {
		List<List<String>> data = new ArrayList<>();

		data.add(TITLE_LIST);

		for (int i = 0; i < clientList.size(); i++) {
			List<String> dataLine = new ArrayList<>();

			dataLine.add(clientList.get(i).getId() + " ");
			dataLine.add(clientList.get(i).getName() + " ");
			dataLine.add(clientList.get(i).getStatus() + " ");
			dataLine.add(clientList.get(i).getCreated() + " ");
			dataLine.add(clientList.get(i).getContactPerson().getName() + " ");
			dataLine.add(clientList.get(i).getContactPerson().getEmail() + " ");
			dataLine.add(clientList.get(i).getAddress().getCity() + " ");
			dataLine.add(clientList.get(i).getAddress().getRegion() + " ");
			dataLine.add(clientList.get(i).getUser().getName() + " ");

			data.add(dataLine);

		}
		// print pdf
		try {
			makeTablePdf(filename, data);
			return "Pdf " + filename + " printed";
		} catch (IOException e) {
			e.printStackTrace();
			return "Problems encountered generating pdf";
		}
	}

	/**Prints given List<List<String>> data into pdf file
	 * 
	 * @param filename without .pdf, with folder path
	 * @param data List of List of Strings to print
	 * @throws IOException
	 */
	private void makeTablePdf(String filename, List<List<String>> data) throws IOException {

		File file = new File(filename + TYPE);
		file.getParentFile().mkdirs();

		// Initialize PDF writer
		PdfWriter writer = new PdfWriter(filename + TYPE);

		// Initialize PDF document
		PdfDocument pdf = new PdfDocument(writer);

		// Initialize document
		Document document = new Document(pdf);

		document.add(new Paragraph(filename));

		Table table = new Table(data.get(0).size());

		for (List<String> record : data) {
			for (String field : record) {
				Cell cell = new Cell().add(new Paragraph(field));
				table.addCell(cell);
				System.out.println(field);
			}
		}
		document.add(table);
		document.close();
	}
	
	/**Prints pdf with simple test contract for given Contract object
	 * 
	 * @param contract given Contract object
	 * @throws FileNotFoundException
	 */
	public void printContract(Contract contract) throws FileNotFoundException {
		
		String filename = contract.getFilename()+ TYPE;
		File file = new File(filename);
		file.getParentFile().mkdirs();

		// Initialize PDF writer
		PdfWriter writer = new PdfWriter(filename);

		// Initialize PDF document
		PdfDocument pdf = new PdfDocument(writer);

		// Initialize document
		Document document = new Document(pdf);
		

		document.add(new Paragraph(contract.getTitle()));
		
		document.add(new Paragraph(contract.getClient().getName()));
		document.add(new Paragraph(contract.getClient().getAddress().getCity()));
		document.add(new Paragraph(contract.getClient().getAddress().getStreet()));
		document.add(new Paragraph(contract.getClient().getAddress().getPostcode()));
		document.add(new Paragraph(contract.getClient().getNip()));
		document.add(new Paragraph(""));
		
		Company ourCompany = companyRepository.findAll().get(0);
		document.add(new Paragraph(ourCompany.getFullName()));
		document.add(new Paragraph(ourCompany.getMainAddress().getCity()));
		document.add(new Paragraph(ourCompany.getMainAddress().getStreet()));
		document.add(new Paragraph(ourCompany.getMainAddress().getPostcode()));
		document.add(new Paragraph(ourCompany.getNip()));
		document.add(new Paragraph(""));
		
		document.add(new Paragraph("Test contract with value "+contract.getValue().toString()));
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 70; i++) {
			sb.append(" test ");
		}
		document.add(new Paragraph(sb.toString()));
		document.add(new Paragraph(sb.toString()));
		document.add(new Paragraph(sb.toString()));
		document.add(new Paragraph(sb.toString()));

		document.add(new Paragraph(""));
		document.add(new Paragraph(LocalDate.now().toString()));
		
		document.close();
		
	}

}
