package org.springframework.samples.petclinic.pet.grpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.samples.petclinic.grpc.NewPetAttribute;
import org.springframework.samples.petclinic.grpc.PetAttributeList;
import org.springframework.samples.petclinic.grpc.PetAttributeRequest;
import org.springframework.samples.petclinic.grpc.PetAttributeResponse;
import org.springframework.samples.petclinic.grpc.PetAttributeServiceGrpc;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.pet.model.PetAttribute;
import org.springframework.samples.petclinic.pet.service.PetAttributeService;
import org.springframework.samples.petclinic.pet.service.PetTypeService;

import java.util.List;

/**
 * @author Rohit Lalwani
 */
@GrpcService
public class PetAttributeGrpcService extends PetAttributeServiceGrpc.PetAttributeServiceImplBase {

	private final PetAttributeService petAttributeService;

	private final PetTypeService petTypeService;

	public PetAttributeGrpcService(PetAttributeService petAttributeService, PetTypeService petTypeService) {
		this.petAttributeService = petAttributeService;
		this.petTypeService = petTypeService;
	}

	public void GetAttributes(PetAttributeRequest request, StreamObserver<PetAttributeList> responseObserver) {
		List<PetAttribute> attributes = petAttributeService.findByPetTypeId(request.getTypeId());

		PetAttributeList.Builder listBuilder = PetAttributeList.newBuilder();
		for (PetAttribute attr : attributes) {
			PetAttributeResponse response = PetAttributeResponse.newBuilder()
				.setId(attr.getId())
				.setTemperament(attr.getTemperament())
				.setWeight(attr.getWeight())
				.setLength(attr.getLength())
				.build();
			listBuilder.addAttributes(response);
		}

		responseObserver.onNext(listBuilder.build());
		responseObserver.onCompleted();
	}

	public void AddAttribute(NewPetAttribute request, StreamObserver<PetAttributeResponse> responseObserver) {
		PetType petType = petTypeService.findPetTypeById(request.getTypeId());
		if (petType == null) {
			responseObserver.onError(new IllegalArgumentException("Pet type not found"));
			return;
		}

		PetAttribute attr = new PetAttribute();
		attr.setTemperament(request.getTemperament());
		attr.setWeight(request.getWeight());
		attr.setLength(request.getLength());
		attr.setPetType(petType);

		PetAttribute saved = petAttributeService.save(attr);

		PetAttributeResponse response = PetAttributeResponse.newBuilder()
			.setId(saved.getId())
			.setTemperament(saved.getTemperament())
			.setWeight(saved.getWeight())
			.setLength(saved.getLength())
			.build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

}
