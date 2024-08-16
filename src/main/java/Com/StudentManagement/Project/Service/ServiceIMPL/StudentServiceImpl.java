package Com.StudentManagement.Project.Service.ServiceIMPL;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import Com.StudentManagement.Project.CustomExceptions.NegativeRollNoException;
import Com.StudentManagement.Project.CustomExceptions.NullDtoException;
import Com.StudentManagement.Project.Entity.StudentEntity;
import Com.StudentManagement.Project.Repository.StudentDAO;
import Com.StudentManagement.Project.Responses.StudentDTO;
import Com.StudentManagement.Project.Service.StudentService;
import Com.StudentManagement.Project.constants.ErrorConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class StudentServiceImpl implements StudentService {
	private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

	@Autowired
	private StudentDAO dao;

	@Override
	public StudentDTO inserData(StudentDTO dto) {
		if (dto == null) {
			logger.error("StudentDTO is null");
			throw new NullDtoException(ErrorConstants.STUDENT_DTO_ERROR);
		}
		if (dto.getRollNo() != null && dto.getRollNo() < 0) {
			logger.error("Negative roll number: {}", dto.getRollNo());
			throw new NegativeRollNoException(ErrorConstants.NEGATIVE_ROLLNO);
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

		if (CollectionUtils.isEmpty(entities)) {
			throw new RuntimeException(ErrorConstants.NO_STUDENT_FOUND);
		}

		return entities.stream().map(entity -> {
			StudentDTO dto = new StudentDTO();
			BeanUtils.copyProperties(entity, dto);
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public boolean updateData(long id, StudentDTO dto) {
		logger.debug("Updating student data for ID: {}, with data: {}", id, dto);

		if (dto == null) {
		    logger.error("StudentDTO is null");
		    throw new NullDtoException(ErrorConstants.STUDENT_DTO_ERROR);
		}

		StudentEntity entity = dao.findById(id).orElseThrow(() -> {
		    logger.error("Student with ID {} not found", id);
		    return new RuntimeException("Student with ID " + id + " not found.");
		});

		if (dto.getRollNo() != null && dto.getRollNo() < 0) {
		    logger.error("Negative roll number: {}", dto.getRollNo());
		    throw new NegativeRollNoException(ErrorConstants.NEGATIVE_ROLLNO);
		}

		dto.setId(entity.getId());

		BeanUtils.copyProperties(dto, entity);
		dao.save(entity);

		logger.info("Student data updated successfully for ID: {}", id);
		return true;
	}

	@Override
	public boolean deleteData(int rollno) {
	    logger.debug("Deleting student data for roll number: {}", rollno);

	    if (rollno < 0) {
	        logger.error("Negative roll number: {}", rollno);
	        throw new NegativeRollNoException(ErrorConstants.NEGATIVE_ROLLNO);
	    }

	    Optional<StudentDTO> studentOpt = findByRollNo(rollno);
	    if (studentOpt.isEmpty()) {
	        logger.error("Student with roll number {} not found", rollno);
	        throw new RuntimeException("Student with roll number " + rollno + " not found.");
	    }

	    StudentDTO studentDTO = studentOpt.get();
	    StudentEntity studentEntity = new StudentEntity();
	    BeanUtils.copyProperties(studentDTO, studentEntity);

	    dao.delete(studentEntity);
	    logger.info("Student with roll number {} deleted successfully", rollno);
	    return true;
	}
	
	@Override
	public Optional<StudentDTO> findByRollNo(int rollNo) {
	    logger.debug("Finding student by roll number: {}", rollNo);

	    List<StudentEntity> entities = dao.findByRollNo(rollNo);
	    if (entities.isEmpty()) {
	        logger.warn("No student found with roll number: {}", rollNo);
	        return Optional.empty();
	    }

	    StudentDTO dto = new StudentDTO();
	    BeanUtils.copyProperties(entities.get(0), dto);
	    return Optional.of(dto);
	}

}
