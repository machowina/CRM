package pl.coderslab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.entity.Address;


@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
