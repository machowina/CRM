package pl.coderslab.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
public class Client {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	@NotBlank
	private String name;
	
	private String status;
	
	private String nip; 
	
	private LocalDateTime created;
	
	@OneToOne(cascade = CascadeType.ALL, optional = true)
	private ContactPerson contactPerson;
	
	@OneToOne(cascade = CascadeType.ALL, optional = true)
	private Address address;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;
	
	

	
	

	
}
