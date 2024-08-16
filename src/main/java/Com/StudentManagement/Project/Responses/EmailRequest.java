package Com.StudentManagement.Project.Responses;

import java.util.List;

public class EmailRequest {

    private String subject;
    private String body;
    private List<String> emailIds;

    public EmailRequest(String subject, String body, List<String> emailIds) {
        this.subject = subject;
        this.body = body;
        this.emailIds = emailIds;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public List<String> getEmailIds() {
        return emailIds;
    }
}
