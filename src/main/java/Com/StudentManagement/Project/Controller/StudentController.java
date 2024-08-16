package Com.StudentManagement.Project.Controller;

import Com.StudentManagement.Project.Responses.StudentDTO;
import Com.StudentManagement.Project.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/students")
@Validated
public class StudentController {

	@Autowired
	private StudentService service;

	@PostMapping
	public ResponseEntity<StudentDTO> insertData(@Valid @RequestBody StudentDTO dto) {
		StudentDTO createdStudent = service.inserData(dto);
		return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<StudentDTO>> getAllStudents() {
		List<StudentDTO> students = service.getAll();
		return new ResponseEntity<>(students, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Boolean> updateData(@PathVariable long id,@Valid @RequestBody StudentDTO dto) {
		boolean isUpdated = service.updateData(id, dto);
		return new ResponseEntity<>(isUpdated, HttpStatus.OK);
	}

	@DeleteMapping("/{rollno}")
	public ResponseEntity<Boolean> deleteData(@PathVariable int rollno) {
		boolean success = service.deleteData(rollno);
		return new ResponseEntity<>(success, HttpStatus.OK);
	}

	@GetMapping("/rollno/{rollno}")
	public ResponseEntity<StudentDTO> getStudentByRollNo(@PathVariable int rollno) {
		Optional<StudentDTO> student = service.findByRollNo(rollno);
		return student.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
}
