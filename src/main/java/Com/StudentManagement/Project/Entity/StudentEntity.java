package Com.StudentManagement.Project.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "Student")
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String middleName;

    @Column(nullable = true)
    private String surname;

    @Column(unique = true, nullable = false)
    private Integer rollNo;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String schoolName;

    @Column(nullable = false)
    @Email
    private String personalMail;

    @Column(nullable = true)
    @Email
    private String parentsMail;

    @Column(nullable = false)
    private String personalPhoneNumber;

    @Column(nullable = true)
    private String parentsPhoneNumber;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(nullable = false)
    private String semester;

    @Column(nullable = false)
    private Branches branch;
}
