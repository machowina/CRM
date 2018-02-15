package pl.coderslab.service;

import java.util.List;

import pl.coderslab.entity.Client;
import pl.coderslab.entity.User;

public interface ClientService {
	
	/**Returns Client object with given id
	 * 
	 * @param id of the client
	 * @return Client object
	 */
	Client findById(Long id);
	
	/**Returns Client object with given company name
	 * 
	 * @param name of the client company
	 * @return Client object
	 */
	Client findByName(String name);
	
	/**Returns all clients
	 * 
	 * @return List of Client objects
	 */
	List<Client> findAll();
	
	/**Returns String list of possible client statuses
	 * 
	 * @return List of String statuses
	 */
	List<String> getStatusList();
	
	/**Saves Client object, returns its id
	 * 
	 * @param client
	 * @return id of saved Client
	 */
	Long saveClient(Client client);
	
	/**Delete Client with given id
	 * 
	 * @param id
	 */
	void deleteClient(Long id);
	
	/**Save Client with currently logged User as employee managing client
	 * 
	 * @param client
	 * @return id of saved Client object
	 */
	Long saveClientWithLoggedUser(Client client);
	
	/**Returns List of Clients with given User as employee managing client
	 * 
	 * @param user employee managing client
	 * @return
	 */
	List<Client> findByUser(User user);

}
