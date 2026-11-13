ackage org.springframework.samples.petclinic.lab;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class OwnerReport {

    private final Map<String, String> phones = new HashMap<>();

    private final String adminSecret;

    // Corrección 1: el secreto llega desde fuera (variable de entorno, vault...)
    public OwnerReport(String adminSecret) {
        this.adminSecret = adminSecret;
    }

    public void addPhone(String owner, String phone) {
        phones.put(owner, phone);
    }

    public boolean isAdmin(String user, String secret) {
        return "admin".equals(user) && adminSecret != null && adminSecret.equals(secret);
    }

    // Corrección 2: try-with-resources cierra el lector
    public String readFirstLine(String path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(path))) {
            return reader.readLine();
        }
    }

    // Corrección 3: se controla el caso sin teléfono
    public int phoneLength(String owner) {
        String phone = phones.get(owner);
        return (phone != null) ? phone.length() : 0;
    }

    // Corrección 4: la excepción se propaga con contexto
    public int parseAge(String value) {
        try {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Edad no válida: " + value, ex);
        }
    }

    // Corrección 5: una sola implementación reutilizada
    public String formatOwner(String firstName, String lastName, String city) {
        return formatPerson(firstName, lastName, city);
    }

    public String formatVet(String firstName, String lastName, String city) {
        return formatPerson(firstName, lastName, city);
    }

    private String formatPerson(String firstName, String lastName, String city) {
        return lastName.toUpperCase() + ", " + firstName + " (" + city + ")";
    }

}