package pl.coderslab.service;

import java.util.List;

import pl.coderslab.entity.Client;
import pl.coderslab.entity.User;

public interface ClientService {
	
	public Client findById(Long id);
	public Client findByName(String name);
	public List<Client> findAll();
	public List<String> getStatusList();
	public Long saveClient(Client client);
	public void updateClient(Client client);
	public void deleteClient(Long id);
	public Long saveClientWithLoggedUser(Client client);
	

}
