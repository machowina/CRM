package pl.coderslab.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;


@Data
@Entity
public class Notification {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@ManyToOne
	@JoinColumn(name = "event_id")
	private Event event;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	private LocalDateTime created;
	
	private boolean wasRead;
	
	private String content;

}
