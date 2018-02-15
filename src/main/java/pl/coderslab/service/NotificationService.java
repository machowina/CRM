package pl.coderslab.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j;
import pl.coderslab.entity.Event;
import pl.coderslab.entity.Notification;
import pl.coderslab.repository.EventRepository;
import pl.coderslab.repository.NotificationRepository;

@Service
@Log4j
public class NotificationService {

	
	private EventRepository eventRepository;
	private NotificationRepository notificationRepository;

	private List<Event> todayEventList = new ArrayList<>();
	
	public NotificationService(EventRepository eventRepository, NotificationRepository notificationRepository) {
		super();
		this.eventRepository = eventRepository;
		this.notificationRepository = notificationRepository;
	}
	
	//CHECK IF SCHOULD GENERATE ANY NOTIFICATIONS ANY 10 MINUTES
	@Scheduled(fixedDelay = 600000, initialDelay = 5000)
	public void checkIfGenerateNotification() {
		// get today's events
		if ((LocalDateTime.now().getHour() >= 9) && (todayEventList.isEmpty())) {
			List<Event> todayEventList = eventRepository.findByTimeBetween(LocalDateTime.now(),
					LocalDateTime.now().plusDays(1));
			// generate notifications for all today's events
			for (Event event : todayEventList) {
				this.generateNotification(event);
			}
		}
		// clear today's events list
		if ((LocalDateTime.now().getHour() > 22) && (!todayEventList.isEmpty())) {
			todayEventList.clear();
		}

		if (!todayEventList.isEmpty()) {

			for (Event event : todayEventList) {
				// delete event from list if it's in the past
				if (LocalDateTime.now().isAfter(event.getTime())) {
					todayEventList.remove(event);
					// generate notification if event is in 2 hours, notifications are not stopped
					// and last notification was earlier than 20 min ago
				} else if (LocalDateTime.now().plusHours(2).isAfter(event.getTime())) {
					Notification notification = notificationRepository.findFirstByEventOrderByCreatedDesc(event);
					List<Notification> notificationList = notificationRepository.findByEvent(event);
					if ((!notification.isStopped()) && (notificationList.size() < 2)
							&& (notification.getCreated().plusMinutes(20).isAfter(LocalDateTime.now()))) {

						this.generateNotification(event);
					}
					// generate notification if event is in 1 hour, notifications are not stopped
					// and last notification was earlier than 20 min ago
				} else if (LocalDateTime.now().plusHours(1).isAfter(event.getTime())) {
					Notification notification = notificationRepository.findFirstByEventOrderByCreatedDesc(event);
					List<Notification> notificationList = notificationRepository.findByEvent(event);
					if ((!notification.isStopped()) && (notificationList.size() < 3)
							&& (notification.getCreated().plusMinutes(20).isAfter(LocalDateTime.now()))) {

						this.generateNotification(event);
					}
				}
			}
		}

	}
	//GENERATE NOTIFICATION FOR AN EVENT
	public void generateNotification(Event event) {
		Notification notification = new Notification();
		notification.setCreated(LocalDateTime.now());
		notification.setEvent(event);
		notification.setStopped(false);
		notification.setUser(event.getUser());
		notification.setContent("You have "+event.getType()+" with "+event.getClient().getName()+" at "+event.getTime());
		notificationRepository.save(notification);
	}
	

}
