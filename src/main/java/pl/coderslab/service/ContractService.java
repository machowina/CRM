package pl.coderslab.service;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.coderslab.AuthenticationFacade;
import pl.coderslab.entity.Client;
import pl.coderslab.entity.Contract;
import pl.coderslab.entity.User;
import pl.coderslab.repository.ContractRepository;

@Service
public class ContractService {

	@Autowired
	private ContractRepository contractRepository;
	@Autowired
	private ContractService contractService;
	@Autowired
	private UserService userService;
	@Autowired
	private PdfService pdfService;
	@Autowired
	private AuthenticationFacade authenticationFacade;

	public void print(Contract contract) throws FileNotFoundException {

		pdfService.printContract(contract);
	}

	public void sendToSupervisor(Contract contract, User user) {

		while (true) {
			User supervisor = user.getSupervisor();

			if (contract.getValue() >= userService.getMaxContractValue(supervisor)) {
				contract.setAcceptedBy(supervisor);
				contractService.save(contract);
				break;
			}
		}
	}

	public void acceptContract(Contract contract) {
		User user = authenticationFacade.getAuthenticatedUser();
		if (contract.getAcceptedBy().equals(user)) {
			contract.setAccepted(true);
			contractService.save(contract);
		}
	}

	public Contract findById(Long id) {
		return contractRepository.findOne(id);
	}

	public void save(Contract contract) {
		contractRepository.save(contract);
	}
	
	public List<Contract> findByClient(Client client) {
		return contractRepository.findByClient(client);
	}

	public List<Contract> findByAcceptedBy(User user){
		return contractRepository.findByAcceptedBy(user);
	}

}
