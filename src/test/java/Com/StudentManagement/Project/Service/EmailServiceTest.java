package Com.StudentManagement.Project.Service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import Com.StudentManagement.Project.Responses.EmailRequest;
import Com.StudentManagement.Project.Service.ServiceIMPL.EmailService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

	@Mock
	private JavaMailSender mailSender;

	@InjectMocks
	private EmailService emailService;

	@Test
	public void testSendEmail_Success() {
		String to = "recipient@example.com";
		String subject = "Test Subject";
		String body = "Test Body";
		List<String> emailIds = new ArrayList<>(Arrays.asList(to));

		EmailRequest request = new EmailRequest(subject, body, emailIds);

		emailService.sendEmail(request);
		verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
	}
}
