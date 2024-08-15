package Com.StudentManagement.Project.Service.ServiceIMPL;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Com.StudentManagement.Project.CustomExceptions.NegativeRollNoException;
import Com.StudentManagement.Project.CustomExceptions.NullDtoException;
import Com.StudentManagement.Project.Entity.StudentEntity;
import Com.StudentManagement.Project.Repository.StudentDAO;
import Com.StudentManagement.Project.Responses.StudentDTO;
import Com.StudentManagement.Project.Service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {
	@Autowired
	private StudentDAO dao;

	@Override
	public StudentDTO inserData(StudentDTO dto) {
		if (dto == null) {
			throw new NullDtoException("StudentDTO cannot be null.");
		}
		if (dto.getRollNo() != null && dto.getRollNo() < 0) {
			throw new NegativeRollNoException("Roll number cannot be negative.");
		}

		StudentEntity convertToEntity = new StudentEntity();
		BeanUtils.copyProperties(dto, convertToEntity);
		StudentEntity savedEntity = dao.save(convertToEntity);

		BeanUtils.copyProperties(savedEntity, dto);
		return dto;
	}

	@Override
	public List<StudentDTO> getAll() {
		List<StudentEntity> entities = dao.findAll();
		return entities.stream().map(entity -> {
			StudentDTO dto = new StudentDTO();
			BeanUtils.copyProperties(entity, dto);
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public StudentDTO updateData(long id, StudentDTO dto) {
		if (dto == null) {
			throw new NullDtoException("StudentDTO cannot be null.");
		}

		StudentEntity entity = dao.findById(id)
				.orElseThrow(() -> new RuntimeException("Student with ID " + id + " not found."));

		if (dto.getRollNo() != null && dto.getRollNo() < 0) {
			throw new NegativeRollNoException("Roll number cannot be negative.");
		}
		Optional.ofNullable(dto.getName()).ifPresent(entity::setName);
		Optional.ofNullable(dto.getSurname()).ifPresent(entity::setSurname);
		Optional.ofNullable(dto.getRollNo()).ifPresent(entity::setRollNo);
		Optional.ofNullable(dto.getAddress()).ifPresent(entity::setAddress);
		Optional.ofNullable(dto.getSchoolName()).ifPresent(entity::setSchoolName);

		StudentEntity updatedEntity = dao.save(entity);
		BeanUtils.copyProperties(updatedEntity, dto);
		return dto;
	}

	@Override
	public String deleteData(int rollno) {
		if (rollno < 0) {
			throw new NegativeRollNoException("Roll number cannot be negative.");
		}

		StudentEntity entity = dao.findByRollNo(rollno).stream().findFirst()
				.orElseThrow(() -> new RuntimeException("Student with roll number " + rollno + " not found."));

		dao.delete(entity);
		return "Student with roll number " + rollno + " deleted successfully.";
	}
}
