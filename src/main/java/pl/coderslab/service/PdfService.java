package pl.coderslab.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import pl.coderslab.entity.Client;

@Service
public class PdfService {

	private static final String TYPE = ".pdf";
	private static final List<String> TITLE_LIST = Arrays.asList("Id", "Name", "Status", "Created", "Contact name",
			"Contact email", "City", "Region", "Responsible employee");

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

	public void makeTablePdf(String filename, List<List<String>> data) throws IOException {

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

}
