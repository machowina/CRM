package pl.coderslab.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;


@Data
@Entity
public class Contract {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String title;

	private String filename;

	private LocalDateTime created;
	
	private boolean accepted;
	
	@ManyToOne
	@JoinColumn(name = "author_id")
	private User author;
	
	@ManyToOne
	@JoinColumn(name = "acceptedBy_id")
	private User acceptedBy;
	
	@ManyToOne
	@JoinColumn(name = "client")
	private Client client;
	
	private Double value;

}
