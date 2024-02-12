/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.vet;

import java.util.List;

import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
class VetController {

	private final VetRepository vetRepository;

	public VetController(VetRepository clinicService) {
		this.vetRepository = clinicService;
	}

	@GetMapping("/vets.html")
	public String showVetList(@RequestParam(defaultValue = "1") int page, Model model) {
		// Demo: Inject vulnerable method manually.
		simulateVulnerableMethodCall();

		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects so it is simpler for Object-Xml mapping
		Vets vets = new Vets();
		Page<Vet> paginated = findPaginated(page);
		vets.getVetList().addAll(paginated.toList());
		return addPaginationModel(page, paginated, model);
	}

	/*
	 * There are 4 vulnerable method signatures in this application:
	 * 'ch/qos/logback/classic/spi/LoggingEventVO#readObject(Ljava/io/ObjectInputStream;)V
	 * ',
	 * 'org/springframework/core/io/buffer/DefaultDataBuffer#split(I)Lorg/springframework/
	 * core/io/buffer/DataBuffer;
	 * 'org/h2/tools/Backup#process(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;
	 * Z)V
	 * 'ch/qos/logback/core/net/HardenedObjectInputStream#<init>(Ljava/io/InputStream;[
	 * Ljava/lang/String;)V
	 *
	 * This method simulates a vulnerable method call for demo purposes. It seems like no
	 * other code path call any of the vulnerable method, so manually invoking it.
	 *
	 * The following logs will be printed while navigating to
	 * http://localhost:8080/vets.html: Sonatype Runtime Agent - [TIME]: *** Vulnerable
	 * CLASS LOADED [className=org/springframework/core/io/buffer/DefaultDataBuffer] by
	 * the JVM Sonatype Runtime Agent - [TIME]: Assigning label 'Runtime-Class-Loaded' to
	 * component 22d73bef97aff8a74a99 in application: a50576c3cd894d20b24dc0d98eea084b
	 * Sonatype Runtime Agent - [TIME]: Component evaluation for
	 * [ComponentEvaluation{hash='22d73bef97aff8a74a99'}] in application
	 * a50576c3cd894d20b24dc0d98eea084b successful. Result
	 * URL=api/v2/evaluation/applications/a50576c3cd894d20b24dc0d98eea084b/results/
	 * 3d62858ec88e49e0afd552066cb160ad Sonatype Runtime Agent - [TIME]: *** Class with
	 * vulnerable METHOD LOADED
	 * [className=org/springframework/core/io/buffer/DefaultDataBuffer, methodName=split,
	 * methodDescriptor=(I)Lorg/springframework/core/io/buffer/DataBuffer;] by the JVM
	 * Sonatype Runtime Agent - [TIME]: Assigning label 'Runtime-Method-Loaded' to
	 * component 22d73bef97aff8a74a99 in application: a50576c3cd894d20b24dc0d98eea084b
	 * Sonatype Runtime Agent - [TIME]: Component evaluation for
	 * [ComponentEvaluation{hash='22d73bef97aff8a74a99'}] in application
	 * a50576c3cd894d20b24dc0d98eea084b successful. Result
	 * URL=api/v2/evaluation/applications/a50576c3cd894d20b24dc0d98eea084b/results/
	 * 47fa37da85d8447f8c101d4db35ec797 Sonatype Runtime Agent - [TIME]: *** Vulnerable
	 * METHOD CALLED [className=org/springframework/core/io/buffer/DefaultDataBuffer,
	 * methodName=split,
	 * methodDescriptor=(I)Lorg/springframework/core/io/buffer/DataBuffer;] Sonatype
	 * Runtime Agent - [TIME]: Assigning label 'Runtime-Method-Called' to component
	 * 22d73bef97aff8a74a99 in application: a50576c3cd894d20b24dc0d98eea084b Sonatype
	 * Runtime Agent - [TIME]: Component evaluation for
	 * [ComponentEvaluation{hash='22d73bef97aff8a74a99'}] in application
	 * a50576c3cd894d20b24dc0d98eea084b successful. Result
	 * URL=api/v2/evaluation/applications/a50576c3cd894d20b24dc0d98eea084b/results/
	 * 6a969f11748f45abba95870fcd7747bb
	 */
	private void simulateVulnerableMethodCall() {
		DefaultDataBufferFactory defaultDataBufferFactory = new DefaultDataBufferFactory();
		DefaultDataBuffer defaultDataBuffer = defaultDataBufferFactory.allocateBuffer(1024);
		defaultDataBuffer.split(0);
	}

	private String addPaginationModel(int page, Page<Vet> paginated, Model model) {
		List<Vet> listVets = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listVets", listVets);
		return "vets/vetList";
	}

	private Page<Vet> findPaginated(int page) {
		int pageSize = 5;
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return vetRepository.findAll(pageable);
	}

	@GetMapping({ "/vets" })
	public @ResponseBody Vets showResourcesVetList() {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects so it is simpler for JSon/Object mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetRepository.findAll());
		return vets;
	}

}
