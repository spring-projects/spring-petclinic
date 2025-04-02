package org.springframework.samples.petclinic.owner;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SonarIssuesExample {

	private static final Logger LOGGER = Logger.getLogger(SonarIssuesExample.class.getName());

	private String unusedField; // Code Smell: Campo no utilizado

	public static void main(String[] args) {
		SonarIssuesExample example = new SonarIssuesExample();
		example.divisionPorCero(5);
		example.inyeccionSQL("admin", "password");
		example.stringInmutable();
	}

	public void divisionPorCero(int numero) {
		int resultado = numero / 0; // Bug: División por cero
		System.out.println("Resultado: " + resultado);
	}

	public void inyeccionSQL(String usuario, String contrasena) {
		// Vulnerabilidad: Construcción de consulta SQL insegura
		String consulta = "SELECT * FROM usuarios WHERE user = '" + usuario + "' AND password = '" + contrasena + "'";
		System.out.println("Ejecutando consulta: " + consulta);
	}

	public void stringInmutable() {
		// Code Smell: Modificación innecesaria de String en bucle
		String texto = "";
		for (int i = 0; i < 10; i++) {
			texto += i; // Ineficiente, crea múltiples instancias de String
		}
		System.out.println(texto);
	}

}
