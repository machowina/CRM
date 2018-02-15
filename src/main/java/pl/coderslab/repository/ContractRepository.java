package pl.coderslab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.entity.Client;
import pl.coderslab.entity.Contract;
import pl.coderslab.entity.User;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
	List<Contract> findByClient(Client client);
	List<Contract> findByAcceptedBy(User user);

}
