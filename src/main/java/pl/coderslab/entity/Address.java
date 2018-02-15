package pl.coderslab.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Data
public class Address {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String country;
	private String region;
	private String city;
	private String street;
	@Pattern(regexp = "\\d{2}-\\d{3}")
	private String postcode;
	

}
