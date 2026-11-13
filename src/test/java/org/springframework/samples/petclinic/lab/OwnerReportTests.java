package org.springframework.samples.petclinic.lab;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class OwnerReportTests {

	private final OwnerReport report = new OwnerReport("s3cret");

	@Test
	void adminRequiresMatchingSecret() {
		assertThat(report.isAdmin("admin", "s3cret")).isTrue();
		assertThat(report.isAdmin("admin", "other")).isFalse();
		assertThat(report.isAdmin("guest", "s3cret")).isFalse();
	}

	@Test
	void readsFirstLine(@TempDir Path dir) throws IOException {
		Path file = Files.writeString(dir.resolve("owners.txt"), "first\nsecond\n");
		assertThat(report.readFirstLine(file.toString())).isEqualTo("first");
	}

	@Test
	void phoneLengthIsZeroWhenOwnerHasNoPhone() {
		report.addPhone("george", "6085551023");
		assertThat(report.phoneLength("george")).isEqualTo(10);
		assertThat(report.phoneLength("nobody")).isZero();
	}

	@Test
	void parseAgeRejectsInvalidValues() {
		assertThat(report.parseAge("42")).isEqualTo(42);
		assertThatThrownBy(() -> report.parseAge("abc")).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void formatsOwnersAndVets() {
		assertThat(report.formatOwner("George", "Franklin", "Madison")).isEqualTo("FRANKLIN, George (Madison)");
		assertThat(report.formatVet("James", "Carter", "Sun Prairie")).isEqualTo("CARTER, James (Sun Prairie)");
	}

}