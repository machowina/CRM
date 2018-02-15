package pl.coderslab.api;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.coderslab.entity.Client;
import pl.coderslab.service.ClientService;
import pl.coderslab.service.UserService;

@RestController
@RequestMapping("/clients")
public class ClientResource {

	private final ClientService clientService;
	private final UserService userService;

	public ClientResource(ClientService clientService, UserService userService) {
		this.clientService = clientService;
		this.userService = userService;

	}

	@GetMapping("/{id}")
	ResponseEntity getClient(@PathVariable Long id) {
		Client client = clientService.findById(id);
		return ResponseEntity.ok(client);
	}

	@GetMapping
	ResponseEntity getClients() {
		return ResponseEntity.ok(clientService.findAll());
	}

	@DeleteMapping("/{id}")
	ResponseEntity deleteUser(@PathVariable Long id) {
		clientService.deleteClient(id);
		return ResponseEntity.accepted().build();
	}

	@PostMapping("/{userEmail}")
	ResponseEntity createClient(@PathVariable String userEmail, @Valid @RequestBody Client client) {
		client.setUser(userService.findByEmail(userEmail));
		Long id = clientService.saveClient(client);
		return ResponseEntity.ok(id);
	}

	@PutMapping("/{userEmail}")
	ResponseEntity updateUser(@PathVariable String userEmail, @Valid @RequestBody Client client) {
		client.setUser(userService.findByEmail(userEmail));
		clientService.saveClient(client);
		return ResponseEntity.accepted().build();
	}

}
