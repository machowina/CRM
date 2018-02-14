package pl.coderslab.service;

import java.util.List;

import pl.coderslab.entity.User;

public interface UserService {
	
	User findByEmail(String email);
	void saveUser(User user);
	List<User> findAll();
	List<User> findBySupervisor(User user);
	Double getMaxContractValue(User user);

}
