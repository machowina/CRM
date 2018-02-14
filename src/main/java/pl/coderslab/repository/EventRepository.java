package pl.coderslab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

}
