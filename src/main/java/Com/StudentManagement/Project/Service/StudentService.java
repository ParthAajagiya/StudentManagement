package Com.StudentManagement.Project.Service;

import java.util.List;
import java.util.Optional;

import Com.StudentManagement.Project.Responses.StudentDTO;

public interface StudentService {
	StudentDTO inserData(StudentDTO dto);
	List<StudentDTO> getAll();
	boolean updateData(long id,StudentDTO dto);
	boolean deleteData(int rollno);
	Optional<StudentDTO> findByRollNo(int rollNo);
}
