package org.springframework.samples.petclinic;

import org.springframework.samples.petclinic.jdbcSevice.Query;

import java.sql.*;

public class JDBCApplication {

	public static void main(String[] args) throws ClassNotFoundException {
		addOwner();
		addPet();
//		deletePet();
//		deleteOwner();
	}

	private static void addOwner() throws ClassNotFoundException {
		String sql = "INSERT INTO owners (first_name, last_name, address, city, telephone) " +
			"VALUES (?,?,?,?,?)";
		String[] values = {"Javier", "Polo", "Avenida Santa Cruz", "Avila", "566666666"};
		Query.insertIntoDelete(sql, values);
	}

	private static void addPet() throws ClassNotFoundException {
		String sql = "INSERT INTO pets (name, birth_date, type_id, owner_id) "
			+
			"VALUES (?,?,?,?)";
		Object[] values = {"Aixa", null, 1, 1};
		Query.insertIntoDelete(sql, values);
	}

	private static void deletePet() throws ClassNotFoundException{
		String sql = "DELETE FROM pets " +
			"WHERE name LIKE ?";
		Object[] values = {"Aixa"};
		Query.insertIntoDelete(sql, values);
	}

	private static void deleteOwner() throws ClassNotFoundException{
		String sql = "DELETE FROM owners " +
			"WHERE first_name LIKE ?";
		Object[] values = {"Javier"};
		Query.insertIntoDelete(sql, values);
	}
}
