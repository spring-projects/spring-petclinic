package org.springframework.samples.petclinic.owner;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MongoDBQueryHandler {

	private static final String DB_URL = "localhost"; // Cambia esto según tu
														// configuración

	private static final int DB_PORT = 27017;

	private static final String DB_NAME = "myDatabase";

	public void handleRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String user = req.getParameter("user");
		String city = req.getParameter("city");

		if (user == null || city == null) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
			return;
		}

		try (MongoClient mongoClient = new MongoClient(DB_URL, DB_PORT)) {
			MongoDatabase database = mongoClient.getDatabase(DB_NAME);
			MongoCollection<Document> collection = database.getCollection("users");

			Document query = new Document("user", user).append("city", city);
			for (Document doc : collection.find(query)) {
				System.out.println(doc.toJson()); // Manejar la salida según sea necesario
			}
		}
		catch (Exception e) {
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
		}
	}

}
