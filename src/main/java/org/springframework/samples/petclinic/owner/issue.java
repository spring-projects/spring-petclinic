package org.springframework.samples.petclinic.owner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class issue {

	public void obtenerUsuarioPorCorreo(String correoElectronico, String contrasena) {
		Connection conexion = null;
		Statement declaracion = null;
		ResultSet resultado = null;

		try {
			// Establecer la conexión con la base de datos
			conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/miBaseDeDatos", "usuario",
					"contrasena");

			// Crear la declaración
			declaracion = conexion.createStatement();

			// Construir la consulta SQL directamente con datos proporcionados por el
			// usuario (vulnerable a inyección SQL)
			String consulta = "SELECT * FROM Usuarios WHERE correo_electronico = '" + correoElectronico
					+ "' AND contrasena = '" + contrasena + "'";

			// Ejecutar la consulta
			resultado = declaracion.executeQuery(consulta);

			// Procesar el resultado
			while (resultado.next()) {
				System.out.println("Usuario encontrado: " + resultado.getString("nombre"));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			// Cerrar recursos en el bloque finally
			if (resultado != null) {
				try {
					resultado.close();
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (declaracion != null) {
				try {
					declaracion.close();
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conexion != null) {
				try {
					conexion.close();
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
