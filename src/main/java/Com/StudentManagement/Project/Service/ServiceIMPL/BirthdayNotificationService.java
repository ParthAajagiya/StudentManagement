package Com.StudentManagement.Project.Service.ServiceIMPL;

import Com.StudentManagement.Project.Entity.StudentEntity;
import Com.StudentManagement.Project.Repository.StudentDAO;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BirthdayNotificationService {
	@Autowired
	private EmailService emailService;

	@Autowired
	private StudentDAO dao;

	@Scheduled(cron = "0 32 9 * * ?")
	public void sendBirthdayEmails() {
		try {
			LocalDate today = LocalDate.now();
			int month = today.getMonthValue();
			int day = today.getDayOfMonth();
			List<StudentEntity> studentsWithBirthday = dao.findByDob(month, day);
			for (StudentEntity student : studentsWithBirthday) {
				try {
					String email = student.getParentsMail();
					if (email != null && !email.isEmpty()) {
						String subject = "Happy Birthday!";
						String body = String.format("Dear %s,\n\nHappy Birthday!\n\nBest wishes,\nYour School",
								student.getName());
						emailService.sendEmail(email, subject, body);
						System.out.println("Birthday email sent to " + email + " for student " + student.getName());
					} else {
						System.out.println("Email not provided for student: " + student.getName());
					}
				} catch (Exception e) {
					System.out.println("Error sending birthday email for student: " + student.getName() + " " + e);
				}
			}
		} catch (Exception e) {
			System.out.println("Error in birthday email service" + e);
		}
	}
}
