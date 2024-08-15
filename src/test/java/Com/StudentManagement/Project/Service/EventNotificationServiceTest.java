package Com.StudentManagement.Project.Service;

import Com.StudentManagement.Project.Entity.EventsEntity;
import Com.StudentManagement.Project.Entity.StudentEntity;
import Com.StudentManagement.Project.Entity.TypesOfEvents;
import Com.StudentManagement.Project.Repository.EventsDao;
import Com.StudentManagement.Project.Repository.StudentDAO;
import Com.StudentManagement.Project.Service.ServiceIMPL.EmailService;
import Com.StudentManagement.Project.Service.ServiceIMPL.EventNotificationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EventNotificationServiceTest {

    @InjectMocks
    private EventNotificationService eventNotificationService;

    @Mock
    private EmailService emailService;

    @Mock
    private StudentDAO studentDAO;

    @Mock
    private EventsDao eventsDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testNotifyBeforeEvents() {
        LocalDate today = LocalDate.now();
        LocalDate targetDate = today.plusDays(2);
        
        EventsEntity event = new EventsEntity();
        event.setEventDate(targetDate);
        event.setName("Annual Day");
        event.setType(TypesOfEvents.ALL);

        StudentEntity student = new StudentEntity();
        student.setParentsMail("parent@example.com");
        
        List<EventsEntity> events = Arrays.asList(event);
        List<StudentEntity> students = Arrays.asList(student);

        when(eventsDAO.findByEventDate(targetDate)).thenReturn(events);
        when(studentDAO.findAll()).thenReturn(students);

        eventNotificationService.notifyBeforeEvents();

        verify(emailService, times(2)).sendEmail(eq("parent@example.com"), any(String.class), any(String.class));
    }
}
