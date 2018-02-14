package pl.coderslab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
