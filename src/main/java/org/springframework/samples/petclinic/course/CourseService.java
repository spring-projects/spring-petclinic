package org.springframework.samples.petclinic.course;

import lombok.RequiredArgsConstructor;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseService {
	private final CouresRepository courseRepository;
	private final VetRepository vetRepository;


	@Transactional()
	public Course createCourse(String name, String description, String difficulty, Integer vetId) {
		Collection<Vet> all = vetRepository.findAll();

		all.forEach(course -> {
			System.out.println(course.getId());
		});

		Vet instructor = vetRepository.findById(vetId)
			.orElseThrow(() -> new RuntimeException("Vet not found"));

		Course course = new Course();
		course.setName(name);
		course.setDescription(description);
		course.setDifficulty(difficulty);
		course.setInstructor(instructor);
		courseRepository.save(course);

		return course;
	}
}
