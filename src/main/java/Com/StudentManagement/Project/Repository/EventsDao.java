package Com.StudentManagement.Project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Com.StudentManagement.Project.Entity.EventsEntity;
import java.util.List;
import java.time.LocalDate;


public interface EventsDao extends JpaRepository<EventsEntity,Long> {
	List<EventsEntity> findByEventDate(LocalDate eventDate);
}
