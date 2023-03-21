package org.springframework.samples.petclinic.records;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
	public NewRecordModel newRecordsTransfer(@RequestBody InputStream body) throws IOException, SQLException {
		final var id = saveToRecordsSystem(body);
		return new NewRecordModel(id);
	}

	private String saveToRecordsSystem(final InputStream is) throws IOException, SQLException {
		final var id = UUID.randomUUID().toString();
		final var path = recordsDir.resolve("record-" + id + ".json");
		Files.copy(is, path);
		final var connection = dataSource.getConnection();
		final PreparedStatement statement = connection.prepareStatement("INSERT INTO records (path) VALUES ?");
		statement.setString(1, path.toString());
		statement.executeUpdate();
		return id;
	}

}
