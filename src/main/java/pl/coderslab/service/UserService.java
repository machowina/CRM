package pl.coderslab.service;

import java.util.List;

import pl.coderslab.entity.User;

public interface UserService {
	
	public User findByEmail(String email);
	public void saveUser(User user);
	public List<User> findAll();
	public List<User> findBySupervisor(User user);

}
