package pl.coderslab.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Entity
@Data
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "role_id")
	private Long id;
	
	@NotBlank
	@Column(unique = true)
	private String name;
	
	private Double maxContractValue;

}
