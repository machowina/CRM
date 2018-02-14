package pl.coderslab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import pl.coderslab.entity.User;
import pl.coderslab.service.UserService;

@Component
public class AuthenticationFacade {
	
	@Autowired
	private UserService userService;
	
	public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
	
	public User getAuthenticatedUser() {
		String email = this.getAuthentication().getName();
		return userService.findByEmail(email);
	}

}
