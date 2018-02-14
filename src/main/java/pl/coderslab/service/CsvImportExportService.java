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
	
	@Autowired
	ClientMapper clientMapper;
	
	public List<Client> readCsvWithHeader(String filename) throws IOException {
		
		Reader reader;
		reader = Files.newBufferedReader(Paths.get(filename+".csv"));
		
		CsvToBean csvToBean = new CsvToBeanBuilder(reader)
                .withType(ClientDto.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
		
		List<ClientDto> clientDtoList = csvToBean.parse();
		List<Client> clientList = new ArrayList<>();
		
		for (Iterator iterator = clientDtoList.iterator(); iterator.hasNext();) {
			ClientDto clientDto = (ClientDto) iterator.next();
			clientList.add(clientMapper.toEntity(clientDto));
		}
		return clientList;
	}
	
	
	public void writeCsv(String filename, List<Client> clientList) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
			
		Writer writer = Files.newBufferedWriter(Paths.get(filename+".csv"));
		
		StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .build();
		
		List<ClientDto> clientDtoList = new ArrayList<ClientDto>();
		
		for (Iterator iterator = clientList.iterator(); iterator.hasNext();) {
			Client client = (Client) iterator.next();
			ClientDto clientDto = clientMapper.toDto(client);
			clientDtoList.add(clientDto);
		}
		beanToCsv.write(clientDtoList);
		
	}
	

}
