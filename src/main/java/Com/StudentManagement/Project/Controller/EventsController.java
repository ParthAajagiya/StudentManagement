package Com.StudentManagement.Project.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Com.StudentManagement.Project.Responses.EventsDTO;
import Com.StudentManagement.Project.Service.EventService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/events")
public class EventsController {

	private static final Logger logger = LoggerFactory.getLogger(EventsController.class);

	@Autowired
	private EventService service;

	@PostMapping
	public ResponseEntity<EventsDTO> createEvent(@RequestBody EventsDTO dto) {
		logger.info("Request to create event: {}", dto);
		EventsDTO createdEvent = service.inserData(dto);
		logger.info("Event created successfully: {}", createdEvent);
		return ResponseEntity.ok(createdEvent);
	}

	@GetMapping
	public ResponseEntity<List<EventsDTO>> getAllEvents() {
		logger.info("Request to fetch all events");
		List<EventsDTO> events = service.getAll();
		logger.info("Fetched {} events", events.size());
		return ResponseEntity.ok(events);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Boolean> updateEvent(@PathVariable long id, @RequestBody EventsDTO dto) {
		logger.info("Request to update event with ID: {}", id);
		boolean isUpdated = service.updateData(id, dto);
		if (isUpdated) {
			logger.info("Event with ID: {} updated successfully", id);
			return ResponseEntity.ok(true);
		} else {
			logger.warn("Event with ID: {} not found", id);
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/date/{eventDate}")
	public ResponseEntity<Boolean> deleteEventByDate(@PathVariable String eventDate) {
		logger.info("Request to delete events with date: {}", eventDate);
		LocalDate date = LocalDate.parse(eventDate);
		boolean isDeleted = service.deleteData(date);
		if (isDeleted) {
			logger.info("Events with date: {} deleted successfully", eventDate);
			return ResponseEntity.ok(true);
		} else {
			logger.warn("No events found for date: {}", eventDate);
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/date/{eventDate}")
	public ResponseEntity<EventsDTO> findByEventDate(@PathVariable String eventDate) {
		logger.info("Request to find event by date: {}", eventDate);
		LocalDate date = LocalDate.parse(eventDate);
		Optional<EventsDTO> event = service.findByEventDate(date);
		return event.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

}
