package org.springframework.samples.petclinic.parking;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.AvailableSlot;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/parking")
public class ParkingController {
	static List<AvailableSlot> availableSlotList = new ArrayList<>();
	Logger logger = LoggerFactory.getLogger(ParkingController.class);

	@GetMapping(value = "/get", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> getData() {
		logger.info("Request received to get available slots and location.");
		AvailableSlot availableSlot1 = new AvailableSlot("1", "10 meter to right");
		AvailableSlot availableSlot2 = new AvailableSlot("3", "11 meter to left");
		AvailableSlot availableSlot3 = new AvailableSlot("9", "20 meter to left");
		availableSlotList.add(availableSlot1);
		availableSlotList.add(availableSlot2);
		availableSlotList.add(availableSlot3);
		return new ResponseEntity<>(availableSlotList.toString(), HttpStatus.OK);
	}

	@PostMapping(value = "/post", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> postData(@RequestBody AvailableSlot body) throws JsonProcessingException {
		AvailableSlot availableSlot = null;
		logger.info("Request received. {}", body.toString());
		availableSlotList.add(body);
		logger.info("Records inserted.");
		return new ResponseEntity<>("Data inserted", HttpStatus.OK);
	}
}
