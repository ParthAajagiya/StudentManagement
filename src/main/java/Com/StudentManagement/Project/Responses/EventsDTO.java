package Com.StudentManagement.Project.Responses;

import java.time.LocalDate;

import Com.StudentManagement.Project.Entity.TypesOfEvents;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventsDTO {
	private Long id;
	@NotNull
	private String name;
	@NotNull
	private TypesOfEvents type;
	@NotNull
	private LocalDate eventDate;
}
