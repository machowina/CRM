package pl.coderslab.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.coderslab.AuthenticationFacade;
import pl.coderslab.entity.Address;
import pl.coderslab.entity.Client;
import pl.coderslab.entity.ContactPerson;
import pl.coderslab.entity.User;
import pl.coderslab.repository.AddressRepository;
import pl.coderslab.repository.ClientRepository;
import pl.coderslab.repository.ContactPersonRepository;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private AuthenticationFacade authenticationFacade;
	
	private final ClientRepository clientRepository;
	private final ContactPersonRepository contactPersonRepository;
	private final AddressRepository addressRepository;

	public ClientServiceImpl(ClientRepository clientRepository,
			ContactPersonRepository contactPersonRepository,AddressRepository addressRepository) {
		this.clientRepository = clientRepository;
		this.contactPersonRepository = contactPersonRepository;
		this.addressRepository = addressRepository;
	}


	@Override
	public Client findById(Long id) {
		return clientRepository.findOne(id);
	}
	
	@Override
	public Client findByName(String name) {
		return clientRepository.findByNameIgnoreCase(name);
	}
	
	@Override
	public List<Client> findAll() {
		return clientRepository.findAll();
	}
	
	@Override
	public List<String> getStatusList() {
		List<String> statusList = Arrays.asList("lead","current","lost");
		return statusList;
	}

	@Override
	public Long saveClientWithLoggedUser(Client client) {
		User user = authenticationFacade.getAuthenticatedUser();
		client.setUser(user);
		client.setCreated(LocalDateTime.now());
		clientRepository.save(client);
		return client.getId();
	}
	@Override
	public Long saveClient(Client client) {
		client.setCreated(LocalDateTime.now());
		clientRepository.save(client);
		return client.getId();
	}

	
	@Override
	public void updateClient(Client client) {
		Client oldClient = clientRepository.findOne(client.getId());
		oldClient.setUser(client.getUser());
		oldClient.setName(client.getName());
		oldClient.setStatus(client.getStatus());
		oldClient.setNip(client.getNip());
		
		if (oldClient.getContactPerson()!=null) {
			ContactPerson oldContact = contactPersonRepository.findOne(oldClient.getContactPerson().getId());
			oldContact.setFirstname(client.getContactPerson().getFirstname());
			oldContact.setLastname(client.getContactPerson().getLastname());
			oldContact.setEmail(client.getContactPerson().getEmail());
			oldContact.setPhone(client.getContactPerson().getPhone());
		} else {
			ContactPerson newContact = client.getContactPerson();
			contactPersonRepository.save(newContact);
			oldClient.setContactPerson(newContact);
		}

		if (oldClient.getAddress()!=null) {
			Address oldAddress = addressRepository.findOne(oldClient.getAddress().getId());
			oldAddress.setCountry(client.getAddress().getCountry());
			oldAddress.setRegion(client.getAddress().getRegion());
			oldAddress.setCity(client.getAddress().getCity());
			oldAddress.setStreet(client.getAddress().getStreet());
			oldAddress.setPostcode(client.getAddress().getPostcode());
		} else {
			Address newAddress = client.getAddress();
			addressRepository.save(newAddress);
			oldClient.setAddress(newAddress);
		}
		clientRepository.save(oldClient);
	}


	@Override
	public void deleteClient(Long id) {
		clientRepository.delete(id);
	}


	




	



}
