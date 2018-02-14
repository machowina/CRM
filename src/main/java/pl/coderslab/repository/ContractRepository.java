package pl.coderslab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.entity.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

}
