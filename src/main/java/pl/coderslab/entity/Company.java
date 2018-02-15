package pl.coderslab.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Pattern;

import lombok.Data;


@Data
@Entity
public class Company {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private String fullName;
	
	@OneToOne
	private Address mainAddress;
	
	@Pattern(regexp = "\\d{10}")
	private String nip;
	
	@OneToMany
	(mappedBy = "address")
	private List<Office> offices;
	
}
