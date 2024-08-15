package Com.StudentManagement.Project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import Com.StudentManagement.Project.Entity.StudentEntity;
import java.util.List;
import java.time.LocalDate;



public interface StudentDAO extends JpaRepository<StudentEntity, Long> {
		List<StudentEntity> findByRollNo(Integer rollNo);
//		List<StudentEntity> findByDob(LocalDate dob);

	    @Query("SELECT s FROM StudentEntity s WHERE EXTRACT(MONTH FROM s.dob) = ?1 AND EXTRACT(DAY FROM s.dob) = ?2")
	    List<StudentEntity> findByDob(int month, int day);
}
