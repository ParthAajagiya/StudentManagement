package Com.StudentManagement.Project.Service.ServiceIMPL;

import Com.StudentManagement.Project.Entity.StudentEntity;
import Com.StudentManagement.Project.Repository.StudentDAO;
import Com.StudentManagement.Project.Responses.EmailRequest;
import Com.StudentManagement.Project.constants.ApplicationConstants;
import Com.StudentManagement.Project.constants.ErrorConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

@Component
public class BirthdayNotificationJob {

	private static final Logger logger = LoggerFactory.getLogger(BirthdayNotificationJob.class);

	@Autowired
	private EmailService emailService;

	@Autowired
	private StudentDAO dao;

	@Scheduled(cron = "${cron.birthday.email}")
	public void sendBirthdayEmails() {
		try {
			LocalDate today = LocalDate.now();
			int month = today.getMonthValue();
			int day = today.getDayOfMonth();
			List<StudentEntity> studentsWithBirthday = dao.findByDob(month, day);

			for (StudentEntity student : studentsWithBirthday) {
				String email = student.getPersonalMail();
				if (email != null && !email.isEmpty()) {
					String subject = ApplicationConstants.BIRTHDAY_EMAIL_SUBJECT;
					String body = String.format(ApplicationConstants.BIRTHDAY_EMAIL_BODY, student.getName());

					EmailRequest emailRequest = new EmailRequest(subject, body, List.of(email));
					emailService.sendEmail(emailRequest);

					logger.info("Birthday email sent to {} for student {}", email, student.getName());
				} else {
					logger.warn("{} {}", ErrorConstants.EMAIL_NOT_PROVIDED, student.getName());
				}
			}
		} catch (Exception e) {
			logger.error(ErrorConstants.ERROR_IN_BIRTHDAY_MAIL, e);
		}
	}
}
