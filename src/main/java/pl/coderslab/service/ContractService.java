package pl.coderslab.service;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j;
import pl.coderslab.AuthenticationFacade;
import pl.coderslab.entity.Client;
import pl.coderslab.entity.Contract;
import pl.coderslab.entity.User;
import pl.coderslab.repository.ContractRepository;

@Log4j
@Service
public class ContractService {

	@Autowired
	private ContractRepository contractRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private PdfService pdfService;
	@Autowired
	private AuthenticationFacade authenticationFacade;

	public ContractService(ContractRepository contractRepository, UserService userService, PdfService pdfService,
			AuthenticationFacade authenticationFacade) {
		super();
		this.contractRepository = contractRepository;
		this.userService = userService;
		this.pdfService = pdfService;
		this.authenticationFacade = authenticationFacade;
	}

	/**Uses PdfService to print given Contract in pdf
	 * 
	 * @param contract
	 * @throws FileNotFoundException
	 */
	public void print(Contract contract) throws FileNotFoundException {
		pdfService.printContract(contract);
	}

	/**Seeks first supervisor in command tree who will be able to accept the contract.
	 * Sets "acceptedBy" field in contract as found supervisor.
	 * 
	 * @param contract
	 * @param user who is not permitted to accept this contract
	 * @return true if correct supervisor found
	 */
	public boolean sendToSupervisor(Contract contract, User user) {
		User supervisor;
		while (true) {
			//log.info("inside the loop");
			supervisor = user.getSupervisor();
			user = supervisor;
			if (supervisor == null) {
				return false;
			}
			//log.info(contract.getValue());
			//log.info(userService.getMaxContractValue(supervisor));
			if (contract.getValue() <= userService.getMaxContractValue(supervisor)) {
				contract.setAcceptedBy(supervisor);
				this.save(contract);
				return true;
			}
		}
	}

	/**Accepts contract if logged user is permitted to accept it
	 * 
	 * @param contract
	 */
	public void acceptContract(Contract contract) {
		User user = authenticationFacade.getAuthenticatedUser();
		//log.info(user);
		//log.info(contract.getAcceptedBy());
		if (contract.getAcceptedBy().equals(user)) {
			contract.setAccepted(true);
			this.save(contract);
		}
	}

	/**Returns contract with given id
	 * 
	 * @param id
	 * @return Contract object
	 */
	public Contract findById(Long id) {
		return contractRepository.findOne(id);
	}

	/**Saves given contract to database
	 * 
	 * @param contract
	 */
	public void save(Contract contract) {
		contractRepository.save(contract);
	}

	/**Returns list of Contracts for given Client
	 * 
	 * @param client
	 * @return list of Contracts
	 */
	public List<Contract> findByClient(Client client) {
		return contractRepository.findByClient(client);
	}

	/**Returns list of Contracts for given User who is responsible for accepting contract
	 * 
	 * @param user responsible for accepting contract
	 * @return list of Contracts
	 */
	public List<Contract> findByAcceptedBy(User user) {
		return contractRepository.findByAcceptedBy(user);
	}

}
