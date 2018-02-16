package pl.coderslab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.entity.Event;
import pl.coderslab.entity.Notification;
import pl.coderslab.entity.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	Notification findFirstByEventOrderByCreatedDesc(Event event);
	List<Notification> findByEvent(Event event);
	List<Notification> findByUser(User user);
	
}
