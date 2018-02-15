package pl.coderslab.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;


@Data
@Entity
public class User {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@NotBlank
	@Email
	@Column(unique = true)
    private String email;
	
	@NotBlank
	private String password;
	
	@NotBlank
	private String firstname;
	
	@NotBlank
	private String lastname;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
	@JoinTable
	private Set<Role> roles;
	
	private boolean active;
	
	private String phone;
	
	@ManyToOne
	@JoinColumn(name = "office_id")
	private Office office;
	
	@ManyToOne
	@JoinColumn(name = "supervisor_id")
	private User supervisor;
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "user")
	private List<Client> clients;
	
	
	
	public String getName() {
		return getFirstname() +" "+ getLastname();
	}
	

}
