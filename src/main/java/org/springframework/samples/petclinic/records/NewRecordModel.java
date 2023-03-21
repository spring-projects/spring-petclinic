package org.springframework.samples.petclinic.records;

public class NewRecordModel {

	private final String id;

	public NewRecordModel(final String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
