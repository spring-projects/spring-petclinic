package org.springframework.samples.petclinic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCApplication {

	static Statement statement = null;

	public static void main(String[] args) {
		System.out.println("-------- Test de conexión con MySQL ------------");

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("No encuentro el driver en el Classpath");
			e.printStackTrace();
			return;
		}

		System.out.println("\nDriver instalado y funcionando");
		Connection connection = null;

		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/petclinic", "root", "root");
			if (connection != null)
				System.out.println("\nConexión establecida");

			statement = connection.createStatement();

			// --------------------LISTADO DE DATOS INICIAL-------------------------//
			listadoDatos();

			/*// -----------------------------INSERT--------------------------------//
			String sql1 = "INSERT INTO owners(first_name, last_name, address, city, telephone)"
					+ " VALUES ('Robert','Gutiérrez','C/ Otra, 7','UnaCiudad','666777888')";

			System.out.println("\nInsertando datos...");

			statement.executeUpdate(sql1);

			// ---------------------------LISTADO DE DATOS----------------------------//
			listadoDatos();

			// -----------------------------UPDATE--------------------------------//

			String sql2 = "UPDATE owners SET `first_name` = 'Carloss', `last_name` = 'Estabango', "
					+ "`address` = '2336 Independence La.5', `city` = 'Waunakes', "
					+ "`telephone` = '5085555485' WHERE (`id` = '10');";

			System.out.println("\nModificando datos...");

			statement.executeUpdate(sql2);
			// ---------------------------LISTADO DE DATOS----------------------------//
			listadoDatos();

			// -----------------------------DELETE------------------------------//

			String sql3 = "DELETE FROM owners WHERE (`id` = '11')";

			System.out.println("\nBorrando datos...");

			statement.executeUpdate(sql3);

			// ---------------------------LISTADO DE DATOS----------------------------//
			listadoDatos();

			System.out.println("\nCierro conexión...");
			statement.close();
			connection.close();*/

			// --------------------------INSERT PREPARADO--------------------------//
			// Estos se recibirian del usuario que los introduce desde un formulario:
			String first_name = "Robert";
			String last_name = "Gutiérrez";
			String address = "C/ Otra, 7";
			String city = "Una Ciudad";
			String telephone = "666777888";

			String insert = "insert into owners (first_name, last_name, address, city, telephone)"
					+ "values(?,?,?,?,?)";
			PreparedStatement ps = connection.prepareStatement(insert);

			ps.setString(1, first_name);
			ps.setString(2, last_name);
			ps.setString(3, address);
			ps.setString(4, city);
			ps.setString(5, telephone);
			ps.execute();

			System.out.println("\nInsertando datos...");
			// ---------------------------LISTADO DE DATOS----------------------------//
			listadoDatos();

			// --------------------------UPDATE PREPARADO--------------------------//
			// Estos se recibirian del usuario que los introduce desde un formulario:
			int id = 11;
			first_name = "Roberto";
			last_name = "Jiménez";
			address = "C/ Otra vez, 77";
			city = "Una Ciudad nueva";
			telephone = "888777666";

			String update = "UPDATE owners SET `first_name` =?, `last_name` =?, `address` =?,"
					+ " `city` =?, `telephone` =? WHERE (`id` =?)";
			ps = connection.prepareStatement(update);

			ps.setString(1, first_name);
			ps.setString(2, last_name);
			ps.setString(3, address);
			ps.setString(4, city);
			ps.setString(5, telephone);
			ps.setInt(6, id);
			ps.execute();

			System.out.println("\nModificando datos...");
			// ---------------------------LISTADO DE DATOS----------------------------//
			listadoDatos();

			// --------------------------DELETE PREPARADA---------------------------//
			// Cojo la id de la variable int id = 11; de la sección UPDATE PREPARADO
			// aunque podría coger otra desde aquí como si fuera el usuario eligiendo
			String delete = "DELETE FROM owners WHERE (`id` =?)";
			ps = connection.prepareStatement(delete);
			ps.setInt(1, id);
			ps.execute();

			System.out.println("\nBorrando datos...");
			// ---------------------------LISTADO DE DATOS----------------------------//
			listadoDatos();
			System.out.println("\nCierro conexión...");
			statement.close();
			connection.close();
			
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		} finally {
			try {
				if (statement != null)
					connection.close();
			} catch (SQLException se) {

			}
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	// Método para listar los datos de owners
	private static void listadoDatos() throws SQLException {
		String sql = "SELECT * FROM owners";
		ResultSet rs = statement.executeQuery(sql);

		System.out.println("\nListando datos...\n");
		while (rs.next()) {

			int id = rs.getInt("id");
			String first_name = rs.getString("first_name");
			String last_name = rs.getString("last_name");
			String address = rs.getString("address");
			String city = rs.getString("city");
			String telephone = rs.getString("telephone");

			System.out.println("Id: " + id + " |" + "Nombre: " + first_name + " |" + " Apellido: " + last_name + " |"
					+ " Dirección: " + address + " |" + " Ciudad: " + city + " |" + " Teléfono: " + telephone);
		}
		rs.close();
	}
}
