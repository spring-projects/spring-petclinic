package org.springframework.samples.petclinic.adapters.dtos.vet;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class VetsResponse {

	private final List<VetDto> vetList;

}
