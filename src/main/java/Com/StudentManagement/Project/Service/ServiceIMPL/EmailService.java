package Com.StudentManagement.Project.Service.ServiceIMPL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import Com.StudentManagement.Project.Responses.EmailRequest;
import Com.StudentManagement.Project.constants.ApplicationConstants;
import Com.StudentManagement.Project.constants.ErrorConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.from}")
	private String mailFrom;

	public void sendEmail(EmailRequest emailRequest) {
		for (String to : emailRequest.getEmailIds()) {
			try {
				SimpleMailMessage message = new SimpleMailMessage();
				message.setTo(to);
				message.setSubject(emailRequest.getSubject());
				message.setText(emailRequest.getBody());
				message.setFrom(mailFrom);
				mailSender.send(message);

				logger.info(ApplicationConstants.SUCCESS_MAIL, to);
			} catch (Exception e) {
				logger.error(ErrorConstants.FAILED_TO_SEND_MAIL, to, e.getMessage());
			}
		}
	}
}
