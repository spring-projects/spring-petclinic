package org.springframework.samples.petclinic.vet;

import org.springframework.samples.petclinic.model.BaseEntity;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.fail;

public class VetTestUtil {

	static void setPrivateField(Object targetObject, String fieldName, String value) {
		try {
			Field field = BaseEntity.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(targetObject, value);

		}
		catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
