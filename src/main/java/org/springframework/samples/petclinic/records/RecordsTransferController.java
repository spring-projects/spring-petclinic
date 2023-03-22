package org.springframework.samples.petclinic.records;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.zip.ZipInputStream;
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
	public NewRecordModel newRecordsTransfer(@RequestParam("zipped") boolean zipped, @RequestBody InputStream body)
			throws IOException {
		final InputStream is = zipped ? new ZipInputStream(body) : body;
		final String id;
		try (is) {
			id = saveToRecordsSystem(is);
		}
		return new NewRecordModel(id);
	}

	private String saveToRecordsSystem(final InputStream is) throws IOException {
		final var id = UUID.randomUUID().toString();
		final var path = recordsDir.resolve("record-" + id + ".json");
		Files.copy(is, path);
		return path.toString();
	}

}
