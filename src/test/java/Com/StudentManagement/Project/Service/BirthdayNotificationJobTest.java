package Com.StudentManagement.Project.Service;

import Com.StudentManagement.Project.Entity.StudentEntity;
import Com.StudentManagement.Project.Repository.StudentDAO;
import Com.StudentManagement.Project.Responses.EmailRequest;
import Com.StudentManagement.Project.Service.ServiceIMPL.BirthdayNotificationJob;
import Com.StudentManagement.Project.Service.ServiceIMPL.EmailService;
import Com.StudentManagement.Project.constants.ApplicationConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;import java.time.LocalDate;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class BirthdayNotificationJobTest {

    @Mock
    private EmailService emailService;

    @Mock
    private StudentDAO studentDAO;

    @InjectMocks
    private BirthdayNotificationJob birthdayNotificationJob;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendBirthdayEmails() {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        StudentEntity student1 = new StudentEntity();
        student1.setName("John Doe");
        student1.setPersonalMail("john.doe@example.com");
        student1.setDob(today);

        List<StudentEntity> studentsWithBirthday = Collections.singletonList(student1);

        when(studentDAO.findByDob(month, day)).thenReturn(studentsWithBirthday);

        birthdayNotificationJob.sendBirthdayEmails();

        verify(emailService, times(1)).sendEmail(argThat(emailRequest ->
            emailRequest.getSubject().equals(ApplicationConstants.BIRTHDAY_EMAIL_SUBJECT) &&
            emailRequest.getBody().contains(student1.getName()) &&
            emailRequest.getEmailIds().contains(student1.getPersonalMail())
        ));

        verify(studentDAO, times(1)).findByDob(month, day);
    }

    @Test
    void testSendBirthdayEmailsWithNoEmails() {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        StudentEntity student = new StudentEntity();
        student.setName("Jane Doe");
        student.setPersonalMail(""); // No email provided
        student.setDob(today);

        List<StudentEntity> studentsWithBirthday = Collections.singletonList(student);

        when(studentDAO.findByDob(month, day)).thenReturn(studentsWithBirthday);

        birthdayNotificationJob.sendBirthdayEmails();

        verify(emailService, never()).sendEmail(any(EmailRequest.class));
        verify(studentDAO, times(1)).findByDob(month, day);
    }

    @Test
    void testSendBirthdayEmailsWithException() {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        when(studentDAO.findByDob(month, day)).thenThrow(new RuntimeException("Database error"));

        birthdayNotificationJob.sendBirthdayEmails();

        verify(emailService, never()).sendEmail(any(EmailRequest.class));
        verify(studentDAO, times(1)).findByDob(month, day);
    }
}
