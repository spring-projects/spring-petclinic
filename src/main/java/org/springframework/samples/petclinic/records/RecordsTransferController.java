package org.springframework.samples.petclinic.records;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
class RecordsTransferController {

	private final Path recordsDir;

	private final DataSource dataSource;

	@Autowired
	RecordsTransferController(final DataSource dataSource) {
		recordsDir = Path.of("records");
		this.dataSource = dataSource;
	}

	@PostMapping("/records-transfers")
	public NewRecordModel newRecordsTransfer(@RequestParam final String patientId, @RequestBody final InputStream body)
			throws IOException, SQLException {
		validatePatientId(patientId);
		final var id = saveToRecordsSystem(body);
		return new NewRecordModel(id);
	}

	/**
	 * @param patientId id of the patient
	 * @throws SQLException when there is a problem with the database
	 * @throws IllegalArgumentException when the patient does not exist
	 */
	private void validatePatientId(final String patientId) throws SQLException {
		final var connection = dataSource.getConnection();
		final var statement = connection.prepareStatement("SELECT * FROM patients WHERE id = ?");
		statement.setString(1, patientId);
		final var rs = statement.executeQuery();
		if (!rs.next()) {
			throw new IllegalArgumentException("Patient with id " + patientId + " does not exist");
		}
	}

	private String saveToRecordsSystem(final InputStream is) throws IOException {
		final var id = UUID.randomUUID().toString();
		final var path = recordsDir.resolve("record-" + id + ".json");
		Files.copy(is, path);
		return path.toString();
	}

}
