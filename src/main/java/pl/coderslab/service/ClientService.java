package pl.coderslab.service;

import java.util.List;

import pl.coderslab.entity.Client;
import pl.coderslab.entity.User;

public interface ClientService {
	
	Client findById(Long id);
	Client findByName(String name);
	List<Client> findAll();
	List<String> getStatusList();
	Long saveClient(Client client);
	void updateClient(Client client);
	void deleteClient(Long id);
	Long saveClientWithLoggedUser(Client client);
	List<Client> findByUser(User user);

}
