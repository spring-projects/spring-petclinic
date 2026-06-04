package org.springframework.samples.petclinic.adapters.web.vet.dtos;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class VetsResponse {

	private final List<VetDto> vetList;

}
