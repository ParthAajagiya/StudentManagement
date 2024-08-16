package Com.StudentManagement.Project.Service.ServiceIMPL;

import Com.StudentManagement.Project.Entity.EventsEntity;
import Com.StudentManagement.Project.Entity.StudentEntity;
import Com.StudentManagement.Project.Entity.TypesOfEvents;
import Com.StudentManagement.Project.Repository.EventsDao;
import Com.StudentManagement.Project.Repository.StudentDAO;
import Com.StudentManagement.Project.Responses.EmailRequest;
import Com.StudentManagement.Project.constants.ApplicationConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventNotificationJob {

	private static final Logger logger = LoggerFactory.getLogger(EventNotificationJob.class);

	@Autowired
	private EmailService emailService;

	@Autowired
	private StudentDAO studentDAO;

	@Autowired
	private EventsDao eventsDAO;

	public ApplicationConstants appConstants;

	@Scheduled(cron = "${cron.event.notification}")
	public void notifyBeforeEvents() {
		try {
			LocalDate today = LocalDate.now();
			LocalDate targetDate = today.plusDays(2);

			List<EventsEntity> events = eventsDAO.findByEventDate(targetDate);

			for (EventsEntity event : events) {
				if (event.getType() == TypesOfEvents.Student) {
					sendEmailsToStudents(event);
				} else if (event.getType() == TypesOfEvents.Parents) {
					sendEmailsToParents(event);
				} else if (event.getType() == TypesOfEvents.ALL) {
					sendEmailsToStudents(event);
					sendEmailsToParents(event);

				}
			}
		} catch (Exception e) {
			logger.error("Error in event notification job", e);
		}
	}

	private void sendEmailsToStudents(EventsEntity event) {
		List<StudentEntity> students = studentDAO.findAll();

		List<String> emailIds = students.stream().map(StudentEntity::getPersonalMail)
				.filter(email -> email != null && !email.isEmpty()).collect(Collectors.toList());

		if (!emailIds.isEmpty()) {
			String subject = appConstants.STUDENT_MAIL_SUBJECT;
			String body = String.format(appConstants.STUDENT_MAIL_BODY, event.getName(), event.getEventDate());

			EmailRequest emailRequest = new EmailRequest(subject, body, emailIds);
			emailService.sendEmail(emailRequest);

			logger.info("Reminder emails sent to students for event {}", event.getName());
		} else {
			logger.warn("No valid email addresses found for students for event {}", event.getName());
		}
	}

	private void sendEmailsToParents(EventsEntity event) {
		List<StudentEntity> students = studentDAO.findAll();

		List<String> emailIds = students.stream().map(StudentEntity::getParentsMail)
				.filter(email -> email != null && !email.isEmpty()).collect(Collectors.toList());

		if (!emailIds.isEmpty()) {
			String subject = appConstants.PARENT_MAIL_SUBJECT;
			String body = String.format(appConstants.PARENT_MAIL_BODY, event.getName(), event.getEventDate());

			EmailRequest emailRequest = new EmailRequest(subject, body, emailIds);
			emailService.sendEmail(emailRequest);

			logger.info("Reminder emails sent to parents for event {}", event.getName());
		} else {
			logger.warn("No valid email addresses found for parents for event {}", event.getName());
		}
	}
}