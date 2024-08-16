package Com.StudentManagement.Project.Responses;

import java.time.LocalDate;

import Com.StudentManagement.Project.Entity.Branches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor

public class StudentDTO {
	
	private Long id;
	
    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    @Size(max = 100, message = "Middle name can have a maximum of 100 characters")
    private String middleName;

    @Size(max = 100, message = "Surname can have a maximum of 100 characters")
    private String surname;

    @NotNull(message = "Roll number cannot be null")
    private Integer rollNo;

    @NotNull(message = "Address cannot be null")
    private String address;

    @NotNull(message = "School name cannot be null")
    private String schoolName;

    @NotNull(message = "Personal email cannot be null")
    @Email(message = "Invalid email format")
    private String personalMail;

    @Email(message = "Invalid email format")
    private String parentsMail;

    @NotNull(message = "Personal phone number cannot be null")
    @Pattern(regexp = "^\\d{10}$", message = "Personal phone number must be 10 digits")
    private String personalPhoneNumber;

    @Pattern(regexp = "^\\d{10}$", message = "Parent's phone number must be 10 digits")
    private String parentsPhoneNumber;

    @NotNull(message = "Date of birth cannot be null")
    private LocalDate dob; // Date of Birth

    @NotNull(message = "Semester cannot be null")
    @Size(min = 1, max = 10, message = "Semester must be between 1 and 10 characters")
    private String semester;

    @NotNull(message = "Branch cannot be null")
    private Branches branch;
}
