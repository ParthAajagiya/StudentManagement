package Com.StudentManagement.Project.Controller;

import Com.StudentManagement.Project.Responses.StudentDTO;
import Com.StudentManagement.Project.Service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testUpdateData() throws Exception {
        StudentDTO dto = new StudentDTO();
        dto.setId(1L);
        dto.setName("Jane");
        dto.setMiddleName("A.");
        dto.setSurname("Doe");
        dto.setRollNo(123);
        dto.setAddress("123 Main St");
        dto.setSchoolName("Springfield High");
        dto.setPersonalMail("jane.doe@example.com");
        dto.setParentsMail("parents.doe@example.com");
        dto.setPersonalPhoneNumber("1234567890");
        dto.setParentsPhoneNumber("0987654321");
        dto.setDob(LocalDate.of(2005, 5, 15));
        dto.setSemester("5th");
        dto.setBranch("Computer Science");
        when(studentService.updateData(any(Long.class), any(StudentDTO.class))).thenReturn(dto);
        String requestBody = objectMapper.writeValueAsString(dto);
        mockMvc.perform(put("/api/v1/students/1")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane"))
                .andExpect(jsonPath("$.surname").value("Doe"))
                .andExpect(jsonPath("$.dob").value("2005-05-15")); // Ensure date is checked
    }
    
    @Test
    public void testDeleteData() throws Exception {
        String expectedResponse = "Student with roll number 1 deleted successfully.";

        when(studentService.deleteData(any(Integer.class))).thenReturn(expectedResponse);

        mockMvc.perform(delete("/api/v1/students/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    assertEquals(expectedResponse, content);
                });
    }
    
    @Test
    public void testGetAllStudents() throws Exception {
        StudentDTO dto = new StudentDTO();
        dto.setId(1L);
        dto.setName("John");
        dto.setMiddleName("A");
        dto.setSurname("Doe");
        dto.setRollNo(123);
        dto.setAddress("123 Main St");
        dto.setSchoolName("XYZ School");
        dto.setPersonalMail("john.doe@example.com");
        dto.setParentsMail("parent@example.com");
        dto.setPersonalPhoneNumber("1234567890");
        dto.setParentsPhoneNumber("0987654321");
        dto.setDob(LocalDate.of(2000, 1, 1));
        dto.setSemester("5");
        dto.setBranch("Computer Science");

        List<StudentDTO> students = Arrays.asList(dto);

        when(studentService.getAll()).thenReturn(students);

        String expectedResponse = objectMapper.writeValueAsString(students);

        mockMvc.perform(get("/api/v1/students")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    assertEquals(expectedResponse, content);
                });
    }
    
    @Test
    public void testInsertData() throws Exception {
        StudentDTO dto = new StudentDTO();
        dto.setId(1L);
        dto.setName("John");
        dto.setMiddleName("A");
        dto.setSurname("Doe");
        dto.setRollNo(123);
        dto.setAddress("123 Main St");
        dto.setSchoolName("XYZ School");
        dto.setPersonalMail("john.doe@example.com");
        dto.setParentsMail("parent@example.com");
        dto.setPersonalPhoneNumber("1234567890");
        dto.setParentsPhoneNumber("0987654321");
        dto.setDob(LocalDate.of(2000, 1, 1));
        dto.setSemester("5");
        dto.setBranch("Computer Science");

        when(studentService.inserData(any(StudentDTO.class))).thenReturn(dto);

        String requestBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/students")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    StudentDTO responseDto = objectMapper.readValue(content, StudentDTO.class);
                    assertEquals(dto.getId(), responseDto.getId());
                    assertEquals(dto.getName(), responseDto.getName());
                    assertEquals(dto.getMiddleName(), responseDto.getMiddleName());
                    assertEquals(dto.getSurname(), responseDto.getSurname());
                    assertEquals(dto.getRollNo(), responseDto.getRollNo());
                    assertEquals(dto.getAddress(), responseDto.getAddress());
                    assertEquals(dto.getSchoolName(), responseDto.getSchoolName());
                    assertEquals(dto.getPersonalMail(), responseDto.getPersonalMail());
                    assertEquals(dto.getParentsMail(), responseDto.getParentsMail());
                    assertEquals(dto.getPersonalPhoneNumber(), responseDto.getPersonalPhoneNumber());
                    assertEquals(dto.getParentsPhoneNumber(), responseDto.getParentsPhoneNumber());
                    assertEquals(dto.getDob(), responseDto.getDob());
                    assertEquals(dto.getSemester(), responseDto.getSemester());
                    assertEquals(dto.getBranch(), responseDto.getBranch());
                });
    }
}
