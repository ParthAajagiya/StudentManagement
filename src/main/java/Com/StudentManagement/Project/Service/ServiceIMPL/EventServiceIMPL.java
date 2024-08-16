package Com.StudentManagement.Project.Service.ServiceIMPL;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Com.StudentManagement.Project.Entity.EventsEntity;
import Com.StudentManagement.Project.Repository.EventsDao;
import Com.StudentManagement.Project.Responses.EventsDTO;
import Com.StudentManagement.Project.Service.EventService;

@Service
public class EventServiceIMPL implements EventService {

	private static final Logger logger = LoggerFactory.getLogger(EventServiceIMPL.class);

	@Autowired
	private EventsDao repository;

	@Override
	public EventsDTO inserData(EventsDTO dto) {
		try {
			logger.info("Inserting event data: {}", dto);
			EventsEntity entity = new EventsEntity();
			BeanUtils.copyProperties(dto, entity);
			EventsEntity savedEntity = repository.save(entity);
			EventsDTO responseDto = new EventsDTO();
			BeanUtils.copyProperties(savedEntity, responseDto);
			return responseDto;
		} catch (Exception e) {
			logger.error("Error inserting event data: {}", dto, e);
			throw e;
		}
	}

	@Override
	public List<EventsDTO> getAll() {
		try {
			logger.info("Fetching all events");
			List<EventsEntity> entities = repository.findAll();
			return entities.stream().map(entity -> {
				EventsDTO dto = new EventsDTO();
				BeanUtils.copyProperties(entity, dto);
				return dto;
			}).collect(Collectors.toList());
		} catch (Exception e) {
			logger.error("Error fetching all events", e);
			throw e;
		}
	}

	@Override
	public boolean updateData(long id, EventsDTO dto) {
		try {
			logger.info("Updating event with ID: {}", id);
			Optional<EventsEntity> existingEvent = repository.findById(id);
			if (existingEvent.isPresent()) {
				EventsEntity entity = existingEvent.get();
				dto.setId(entity.getId());
				BeanUtils.copyProperties(dto, entity);
				repository.save(entity);
				logger.info("Event updated: {}", dto);
				return true;
			} else {
				logger.warn("Event with ID: {} not found", id);
				return false;
			}
		} catch (Exception e) {
			logger.error("Error updating event with ID: {}", id, e);
			return false;
		}
	}

	@Override
	public boolean deleteData(LocalDate eventDate) {
		try {
			logger.info("Deleting events with date: {}", eventDate);
			List<EventsEntity> events = repository.findByEventDate(eventDate);

			if (!events.isEmpty()) {
				repository.deleteAll(events);
				logger.info("Deleted {} events with date: {}", events.size(), eventDate);
				return true;
			} else {
				logger.warn("No events found for date: {}", eventDate);
				return false;
			}
		} catch (Exception e) {
			logger.error("Error deleting events with date: {}", eventDate, e);
			throw e;
		}
	}

	@Override
	public Optional<EventsDTO> findByEventDate(LocalDate eventDate) {
		try {
			logger.info("Searching event by date: {}", eventDate);
			List<EventsEntity> events = repository.findByEventDate(eventDate);
			if (!events.isEmpty()) {
				EventsEntity entity = events.get(0);
				EventsDTO dto = new EventsDTO();
				BeanUtils.copyProperties(entity, dto);
				return Optional.of(dto);
			} else {
				logger.warn("Event not found for date: {}", eventDate);
				return Optional.empty();
			}
		} catch (Exception e) {
			logger.error("Error searching event by date: {}", eventDate, e);
			throw e;
		}
	}
}
