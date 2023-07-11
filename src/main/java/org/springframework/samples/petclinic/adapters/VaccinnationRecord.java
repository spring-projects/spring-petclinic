package org.springframework.samples.petclinic.adapters;

public record VaccinnationRecord(int recordId, int petId, java.time.Instant vaccineDate) {

}
