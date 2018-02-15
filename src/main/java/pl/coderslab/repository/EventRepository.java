package pl.coderslab.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.entity.Client;
import pl.coderslab.entity.Contract;
import pl.coderslab.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
	List<Event> findByTimeAfter(LocalDateTime dateTime);
	List<Event> findByTimeBetween(LocalDateTime start, LocalDateTime end);
	List<Event> findByClient(Client client);

}
