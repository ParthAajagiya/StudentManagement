package Com.StudentManagement.Project.Service;

import Com.StudentManagement.Project.Entity.EventsEntity;
import Com.StudentManagement.Project.Entity.StudentEntity;
import Com.StudentManagement.Project.Entity.TypesOfEvents;
import Com.StudentManagement.Project.Repository.EventsDao;
import Com.StudentManagement.Project.Repository.StudentDAO;
import Com.StudentManagement.Project.Responses.EmailRequest;
import Com.StudentManagement.Project.Service.ServiceIMPL.EmailService;
import Com.StudentManagement.Project.Service.ServiceIMPL.EventNotificationJob;
import Com.StudentManagement.Project.constants.ApplicationConstants;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class EventNotificationJobTest {

	@Mock
	private EmailService emailService;

	@Mock
	private StudentDAO studentDAO;

	@Mock
	private EventsDao eventsDAO;

	@InjectMocks
	private EventNotificationJob eventNotificationJob;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		ApplicationConstants appConstants = new ApplicationConstants();
		eventNotificationJob.appConstants = appConstants;
	}

	@Test
	void testNotifyBeforeEvents() {
		LocalDate today = LocalDate.now();
		LocalDate targetDate = today.plusDays(2);

		EventsEntity event = new EventsEntity();
		event.setName("Annual Day");
		event.setEventDate(targetDate);
		event.setType(TypesOfEvents.Student);

		List<EventsEntity> events = Collections.singletonList(event);

		StudentEntity student1 = new StudentEntity();
		student1.setPersonalMail("student1@example.com");
		student1.setParentsMail("parent1@example.com");

		StudentEntity student2 = new StudentEntity();
		student2.setPersonalMail("student2@example.com");
		student2.setParentsMail("parent2@example.com");

		List<StudentEntity> students = Arrays.asList(student1, student2);

		when(eventsDAO.findByEventDate(targetDate)).thenReturn(events);
		when(studentDAO.findAll()).thenReturn(students);

		eventNotificationJob.notifyBeforeEvents();

		verify(emailService, times(1)).sendEmail(
				argThat(emailRequest -> (emailRequest.getSubject().equals(ApplicationConstants.STUDENT_MAIL_SUBJECT)
						|| emailRequest.getSubject().equals(ApplicationConstants.PARENT_MAIL_SUBJECT))
						&& emailRequest.getBody().contains("Annual Day")
						&& (emailRequest.getEmailIds()
								.containsAll(Arrays.asList("student1@example.com", "student2@example.com"))
								|| emailRequest.getEmailIds()
										.containsAll(Arrays.asList("parent1@example.com", "parent2@example.com")))));
	}

	@Test
	void testNotifyBeforeEventsWithNoEmails() {
		LocalDate today = LocalDate.now();
		LocalDate targetDate = today.plusDays(2);

		EventsEntity event = new EventsEntity();
		event.setName("Annual Day");
		event.setEventDate(targetDate);
		event.setType(TypesOfEvents.Student);

		List<EventsEntity> events = Collections.singletonList(event);

		StudentEntity student = new StudentEntity();
		student.setPersonalMail("");
		student.setParentsMail("");

		List<StudentEntity> students = Collections.singletonList(student);

		when(eventsDAO.findByEventDate(targetDate)).thenReturn(events);
		when(studentDAO.findAll()).thenReturn(students);

		eventNotificationJob.notifyBeforeEvents();

		verify(emailService, never()).sendEmail(any(EmailRequest.class));
	}

	@Test
	void testNotifyBeforeEventsWithException() {
		LocalDate today = LocalDate.now();
		LocalDate targetDate = today.plusDays(2);

		when(eventsDAO.findByEventDate(targetDate)).thenThrow(new RuntimeException("Database error"));

		eventNotificationJob.notifyBeforeEvents();

		verify(emailService, never()).sendEmail(any(EmailRequest.class));
	}
}