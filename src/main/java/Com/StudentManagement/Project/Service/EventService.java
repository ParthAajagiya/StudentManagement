package Com.StudentManagement.Project.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import Com.StudentManagement.Project.Responses.EventsDTO;

public interface EventService {
	EventsDTO inserData(EventsDTO dto);
	List<EventsDTO> getAll();
	boolean updateData(long id,EventsDTO dto);
	boolean deleteData(LocalDate eventDate);
	Optional<EventsDTO> findByEventDate(LocalDate eventDate);
}
