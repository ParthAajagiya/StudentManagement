package Com.StudentManagement.Project.Service;

import Com.StudentManagement.Project.Entity.StudentEntity;
import Com.StudentManagement.Project.Repository.StudentDAO;
import Com.StudentManagement.Project.Service.ServiceIMPL.BirthdayNotificationService;
import Com.StudentManagement.Project.Service.ServiceIMPL.EmailService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class BirthdayNotificationServiceTest {

    @Mock
    private EmailService emailService;

    @Mock
    private StudentDAO dao;

    @InjectMocks
    private BirthdayNotificationService birthdayNotificationService;

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
        student1.setParentsMail("john.doe@example.com");

        StudentEntity student2 = new StudentEntity();
        student2.setName("Jane Smith");
        student2.setParentsMail("jane.smith@example.com");

        StudentEntity student3 = new StudentEntity();
        student3.setName("No Email Student");
        student3.setParentsMail("");

        List<StudentEntity> studentsWithBirthday = Arrays.asList(student1, student2, student3);

        when(dao.findByDob(month, day)).thenReturn(studentsWithBirthday);

        birthdayNotificationService.sendBirthdayEmails();

        verify(emailService, times(1)).sendEmail(
                eq("john.doe@example.com"),
                eq("Happy Birthday!"),
                contains("Dear John Doe")
        );

        verify(emailService, times(1)).sendEmail(
                eq("jane.smith@example.com"),
                eq("Happy Birthday!"),
                contains("Dear Jane Smith")
        );

        verify(emailService, never()).sendEmail(
                eq(""),
                anyString(),
                anyString()
        );

        verify(dao, times(1)).findByDob(month, day);
    }

    @Test
    void testSendBirthdayEmailsWithException() {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        when(dao.findByDob(month, day)).thenThrow(new RuntimeException("Database error"));

        birthdayNotificationService.sendBirthdayEmails();

        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }
}