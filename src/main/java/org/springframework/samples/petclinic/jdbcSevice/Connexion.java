package org.springframework.samples.petclinic.jdbcSevice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class Connexion {

	public Connexion(){}

	private static final String DRIVER_URL = "jdbc:mysql://localhost:3306/petClinic";
	private static final String ROOT = "root";
	private static final String PASS = "";
	protected static Connection connexion = null;

	protected static Connection connect() throws ClassNotFoundException, SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		connexion =  DriverManager.getConnection(DRIVER_URL, ROOT, PASS);
		return connexion;
	}

}
