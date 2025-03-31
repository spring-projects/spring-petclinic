package org.springframework.samples.petclinic.owner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class issue {

	// Ejemplo de método vulnerable que usa datos de usuario directamente en la consulta
	// SQL
	public ResultSet insecureLogin(Connection connection, String username, String password) throws SQLException {
		String query = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
		Statement stmt = connection.createStatement();
		return stmt.executeQuery(query); // SonarQube marcará esto como Blocker
	}

	// Método que expone la vulnerabilidad usando parámetros de request simulados
	public ResultSet searchUsers(Connection connection, String searchTerm) throws SQLException {
		String query = "SELECT * FROM users WHERE name = '" + searchTerm + "'";
		Statement stmt = connection.createStatement();
		return stmt.executeQuery(query); // Vulnerabilidad SQL Injection
	}

	// Uso peligroso con concatenación directa
	public static void main(String[] args) {
		try {
			// Simulación de datos controlados por el usuario
			String userInput = "admin' OR '1'='1";
			String passInput = "fake' OR 'x'='x";

			issue example = new issue();
			ResultSet rs = example.insecureLogin(null, userInput, passInput);

			// ... procesar resultados
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

}