package org.springframework.samples.petclinic.course;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.course.dto.CreateCourseRequest;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {
	private final CourseService courseService;


	@PostMapping()
	public ResponseEntity<Integer> createCourse(@RequestBody CreateCourseRequest request) {
		System.out.println(request);

		Course course = courseService.createCourse(request.getName(), request.getDescription(), request.getDescription(), request.getVetId());

		return ResponseEntity.ok(course.getId());
	}
}
