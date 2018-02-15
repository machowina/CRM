package pl.coderslab.service;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import pl.coderslab.dto.ClientDto;
import pl.coderslab.dto.ClientMapper;
import pl.coderslab.entity.Client;

@Service
public class CsvImportExportService {

	private static final String TYPE = ".csv";
	
	@Autowired
	ClientMapper clientMapper;
	@Autowired
	private ClientService clientService;
	

	/**Imports csv file with list of Client objects
	 * 
	 * @param filename without .csv, with folder path
	 * @return list of imported Client objects
	 * @throws IOException
	 */
	public List<Client> readCsvWithHeader(String filename) throws IOException {

		Reader reader;
		reader = Files.newBufferedReader(Paths.get(filename + TYPE));

		CsvToBean csvToBean = new CsvToBeanBuilder(reader).withType(ClientDto.class).withIgnoreLeadingWhiteSpace(true)
				.build();

		List<ClientDto> clientDtoList = csvToBean.parse();
		List<Client> clientList = new ArrayList<>();

		for (ClientDto clientDto : clientDtoList) {
			Client client = clientMapper.toEntity(clientDto);
			clientService.saveClient(client);
			clientList.add(client);
		}

		return clientList;
	}

	/**Exports csv file with list of Client objects
	 * 
	 * @param filename without .csv, with folder path
	 * @param clientList to export
	 * @throws CsvDataTypeMismatchException
	 * @throws CsvRequiredFieldEmptyException
	 * @throws IOException
	 */
	public void writeCsv(String filename, List<Client> clientList)
			throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {

		Writer writer = Files.newBufferedWriter(Paths.get(filename + TYPE));

		StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
				.build();

		List<ClientDto> clientDtoList = new ArrayList<ClientDto>();

		for (Client client : clientList) {
			ClientDto clientDto = clientMapper.toDto(client);
			clientDtoList.add(clientDto);
		}

		beanToCsv.write(clientDtoList);
		writer.close();

	}

}
