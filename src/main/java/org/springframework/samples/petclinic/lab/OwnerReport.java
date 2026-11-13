package org.springframework.samples.petclinic.lab;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OwnerReport {

	// Defecto 1: credencial en el código
	private static final String PASSWORD = "Petclinic2024!";

	private final Map<String, String> phones = new HashMap<>();

	public void addPhone(String owner, String phone) {
		phones.put(owner, phone);
	}

	public boolean isAdmin(String user, String secret) {
		return "admin".equals(user) && PASSWORD.equals(secret);
	}

	// Defecto 2: recurso sin cerrar
	public String readFirstLine(String path) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(path));
		return reader.readLine();
	}

	// Defecto 3: posible NullPointerException
	public int phoneLength(String owner) {
		String phone = null;
		if (phones.containsKey(owner)) {
			phone = phones.get(owner);
		}
		return phone.length();
	}

	// Defecto 4: excepción silenciada
	public int parseAge(String value) {
		int age = 0;
		try {
			age = Integer.parseInt(value);
		}
		catch (NumberFormatException ex) {
		}
		return age;
	}

	// Defecto 5: código duplicado (formatOwner y formatVet son idénticos)
	public String formatOwner(String firstName, String lastName, String city) {
		StringBuilder line = new StringBuilder();
		line.append(lastName.toUpperCase());
		line.append(", ");
		line.append(firstName);
		line.append(" (");
		line.append(city);
		line.append(")");
		return line.toString();
	}

	public String formatVet(String firstName, String lastName, String city) {
		StringBuilder line = new StringBuilder();
		line.append(lastName.toUpperCase());
		line.append(", ");
		line.append(firstName);
		line.append(" (");
		line.append(city);
		line.append(")");
		return line.toString();
	}

}