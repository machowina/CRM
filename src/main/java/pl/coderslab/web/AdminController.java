package pl.coderslab.web;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.coderslab.entity.Client;
import pl.coderslab.entity.Office;
import pl.coderslab.entity.Role;
import pl.coderslab.entity.User;
import pl.coderslab.repository.OfficeRepository;
import pl.coderslab.repository.RoleRepository;
import pl.coderslab.service.UserService;

@Controller
@RequestMapping("admin")
public class AdminController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private OfficeRepository officeRepository;
	
	//ADDING NEW USER
	@GetMapping(path = "/addUser")
	public String addUser(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		 
		Set<Role> allRoles = new HashSet<Role>(roleRepository.findAll());
		model.addAttribute("allRoles", allRoles);
		
		List<User> allUsers = userService.findAll();
		model.addAttribute("allUsers", allUsers);
		
		List<Office> allOffices = officeRepository.findAll();
		model.addAttribute("allOffices", allOffices);
		return "admin/addUser";
	}

	@PostMapping(path = "/addUser")
	public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult bresult) {
		
		if(bresult.hasErrors()) {
            return "admin/addUser";
        } else {
        	userService.saveUser(user);
    		return "admin/success";
        }
	} 
	
	/*//EDITING USER
		@GetMapping(path = "/edit")
		public String editClient(@RequestParam Long id, Model model) {
			
			User user = userService.findById(id);
			model.addAttribute("user", user);
		
			
			return "admin/editUser";
		}
		
		@PostMapping(path = "/edit")
		public String seveClient(@ModelAttribute("client") @Valid User user, BindingResult bresult) {
			
			if(bresult.hasErrors()) {
	            return "client/editClient";
	        } else {
	        	clientService.saveClient(client);
	    		return "client/success";
	        }
		} 
*/

	
}
