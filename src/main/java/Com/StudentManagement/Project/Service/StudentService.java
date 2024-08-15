package Com.StudentManagement.Project.Service;

import java.util.List;

import Com.StudentManagement.Project.Responses.StudentDTO;

public interface StudentService {
	StudentDTO inserData(StudentDTO dto);
	List<StudentDTO> getAll();
	StudentDTO updateData(long id,StudentDTO dto);
	String deleteData(int rollno);
}
