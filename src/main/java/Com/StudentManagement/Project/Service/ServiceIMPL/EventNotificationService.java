package Com.StudentManagement.Project.Service.ServiceIMPL;

import Com.StudentManagement.Project.Entity.EventsEntity;
import Com.StudentManagement.Project.Entity.StudentEntity;
import Com.StudentManagement.Project.Entity.TypesOfEvents;
import Com.StudentManagement.Project.Repository.EventsDao;
import Com.StudentManagement.Project.Repository.StudentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventNotificationService {
	@Autowired
	private EmailService emailService;

	@Autowired
	private StudentDAO studentDAO;

	@Autowired
	private EventsDao eventsDAO;

	@Scheduled(cron = "0 44 18 * * ?")
	public void notifyBeforeEvents() {
		LocalDate today = LocalDate.now();
		LocalDate targetDate = today.plusDays(2); // Day after tomorrow

		List<EventsEntity> events = eventsDAO.findByEventDate(targetDate);

		for (EventsEntity event : events) {
			if (event.getType() == TypesOfEvents.Student || event.getType() == TypesOfEvents.ALL) {
				sendEmailsToStudents(event);
			}
			if (event.getType() == TypesOfEvents.Parents || event.getType() == TypesOfEvents.ALL) {
				sendEmailsToParents(event);
			}
		}
	}

	private void sendEmailsToStudents(EventsEntity event) {
		List<StudentEntity> students = studentDAO.findAll(); // Fetch all students

		for (StudentEntity student : students) {
			String email = student.getParentsMail();
			if (email != null && !email.isEmpty()) {
				String subject = "Reminder: Upcoming Event";
				String body = String.format("Dear Parent,\n\nThis is a reminder for the event \"%s\" happening on the "
						+ event.getEventDate() + ".\n\nBest regards,\nYour School", event.getName());
				emailService.sendEmail(email, subject, body);
				System.out.println("Reminder email sent to " + email + " for event " + event.getName());
			}
		}
	}

	private void sendEmailsToParents(EventsEntity event) {
		List<StudentEntity> students = studentDAO.findAll(); // Fetch all students

		for (StudentEntity student : students) {
			String email = student.getParentsMail();
			if (email != null && !email.isEmpty()) {
				String subject = "Reminder: Upcoming Event";
				String body = String.format(
						"Dear Parent,\n\nThis is a reminder for the event \"%s\" happening the day after tomorrow.\n\nBest regards,\nYour School",
						event.getName());
				emailService.sendEmail(email, subject, body);
				System.out.println("Reminder email sent to " + email + " for event " + event.getName());
			}
		}
	}
}
