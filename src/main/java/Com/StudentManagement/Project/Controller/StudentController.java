package Com.StudentManagement.Project.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import Com.StudentManagement.Project.Responses.StudentDTO;
import Com.StudentManagement.Project.Service.StudentService;
import jakarta.validation.Valid;
import java.util.List;

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
    public ResponseEntity<StudentDTO> updateData(@PathVariable long id, @Valid @RequestBody StudentDTO dto) {
        StudentDTO updatedStudent = service.updateData(id, dto);
        return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }

    @DeleteMapping("/{rollno}")
    public ResponseEntity<String> deleteData(@PathVariable int rollno) {
        String response = service.deleteData(rollno);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
