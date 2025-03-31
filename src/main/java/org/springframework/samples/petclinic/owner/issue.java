import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class UsuarioDAO {

	public Usuario obtenerUsuarioPorCorreo(String correoElectronico, String contrasena) {
		Usuario usuario = null;
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
			if (resultado.next()) {
				usuario = new Usuario();
				usuario.setId(resultado.getInt("id"));
				usuario.setNombre(resultado.getString("nombre"));
				usuario.setCorreoElectronico(resultado.getString("correo_electronico"));
				// Asignar otros atributos según sea necesario
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
		return usuario;
	}

}
