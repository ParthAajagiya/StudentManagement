package Com.StudentManagement.Project.Service;

import Com.StudentManagement.Project.CustomExceptions.NegativeRollNoException;
import Com.StudentManagement.Project.CustomExceptions.NullDtoException;
import Com.StudentManagement.Project.Entity.StudentEntity;
import Com.StudentManagement.Project.Repository.StudentDAO;
import Com.StudentManagement.Project.Responses.StudentDTO;
import Com.StudentManagement.Project.Service.ServiceIMPL.StudentServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StudentServiceImplTest {

    @InjectMocks
    private StudentServiceImpl studentService;

    @Mock
    private StudentDAO studentDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInserData_withValidData() {
        StudentDTO dto = new StudentDTO();
        dto.setRollNo(1);
        dto.setName("John");
        dto.setSurname("Doe");

        StudentEntity entity = new StudentEntity();
        BeanUtils.copyProperties(dto, entity);
        when(studentDAO.save(any(StudentEntity.class))).thenReturn(entity);

        StudentDTO result = studentService.inserData(dto);

        assertNotNull(result);
        assertEquals(dto.getRollNo(), result.getRollNo());
        assertEquals(dto.getName(), result.getName());
    }

    @Test
    public void testInserData_withNullDto() {
        assertThrows(NullDtoException.class, () -> studentService.inserData(null));
    }

    @Test
    public void testInserData_withNegativeRollNo() {
        StudentDTO dto = new StudentDTO();
        dto.setRollNo(-1);

        assertThrows(NegativeRollNoException.class, () -> studentService.inserData(dto));
    }

    @Test
    public void testGetAll() {
        StudentEntity entity = new StudentEntity();
        entity.setRollNo(1);
        entity.setName("John");

        List<StudentEntity> entities = Arrays.asList(entity);
        when(studentDAO.findAll()).thenReturn(entities);

        List<StudentDTO> result = studentService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(entity.getName(), result.get(0).getName());
    }

    @Test
    public void testUpdateData_withValidData() {
        long id = 1L;
        StudentDTO dto = new StudentDTO();
        dto.setName("Jane");
        dto.setSurname("Doe");

        StudentEntity entity = new StudentEntity();
        entity.setId(id);
        entity.setName("John");
        entity.setSurname("Smith");

        when(studentDAO.findById(id)).thenReturn(Optional.of(entity));
        when(studentDAO.save(any(StudentEntity.class))).thenReturn(entity);

        StudentDTO result = studentService.updateData(id, dto);

        assertNotNull(result);
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getSurname(), result.getSurname());
    }

    @Test
    public void testUpdateData_withNullDto() {
        assertThrows(NullDtoException.class, () -> studentService.updateData(1L, null));
    }

    @Test
    public void testUpdateData_withNonExistentId() {
        long id = 1L;
        StudentDTO dto = new StudentDTO();

        when(studentDAO.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> studentService.updateData(id, dto));
    }

    @Test
    public void testDeleteData_withValidRollNo() {
        int rollNo = 1;
        StudentEntity entity = new StudentEntity();
        entity.setRollNo(rollNo);

        when(studentDAO.findByRollNo(rollNo)).thenReturn(Arrays.asList(entity));

        String result = studentService.deleteData(rollNo);

        assertEquals("Student with roll number 1 deleted successfully.", result);
        verify(studentDAO, times(1)).delete(entity);
    }

    @Test
    public void testDeleteData_withNegativeRollNo() {
        assertThrows(NegativeRollNoException.class, () -> studentService.deleteData(-1));
    }

    @Test
    public void testDeleteData_withNonExistentRollNo() {
        int rollNo = 1;

        when(studentDAO.findByRollNo(rollNo)).thenReturn(Arrays.asList());

        assertThrows(RuntimeException.class, () -> studentService.deleteData(rollNo));
    }
}
