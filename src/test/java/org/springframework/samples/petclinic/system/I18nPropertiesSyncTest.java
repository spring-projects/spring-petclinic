package org.springframework.samples.petclinic.system;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * This test ensures that there are no hard-coded strings without internationalization in
 * any HTML files. Also ensures that a string is translated in every language to avoid
 * partial translations.
 *
 * @author Anuj Ashok Potdar
 */
public class I18nPropertiesSyncTest {

	private static final String I18N_DIR = "src/main/resources";

	private static final String BASE_NAME = "messages";

	public static final String PROPERTIES = ".properties";

	private static final Pattern HTML_TEXT_LITERAL = Pattern.compile(">([^<>{}]+)<");

	private static final Pattern BRACKET_ONLY = Pattern.compile("<[^>]*>\\s*[\\[\\]](?:&nbsp;)?\\s*</[^>]*>");

	private static final Pattern HAS_TH_TEXT_ATTRIBUTE = Pattern.compile("th:(u)?text\\s*=\\s*\"[^\"]+\"");

	@Test
	public void checkNonInternationalizedStrings() throws IOException {
		Path root = Paths.get("src/main");
		List<Path> files;

		try (Stream<Path> stream = Files.walk(root)) {
			files = stream.filter(p -> p.toString().endsWith(".java") || p.toString().endsWith(".html"))
				.filter(p -> !p.toString().contains("/test/"))
				.filter(p -> !p.getFileName().toString().endsWith("Test.java"))
				.toList();
		}

		StringBuilder report = new StringBuilder();

		for (Path file : files) {
			List<String> lines = Files.readAllLines(file);
			for (int i = 0; i < lines.size(); i++) {
				String line = lines.get(i).trim();

				if (line.startsWith("//") || line.startsWith("@") || line.contains("log.")
						|| line.contains("System.out"))
					continue;

				if (file.toString().endsWith(".html")) {
					boolean hasLiteralText = HTML_TEXT_LITERAL.matcher(line).find();
					boolean hasThTextAttribute = HAS_TH_TEXT_ATTRIBUTE.matcher(line).find();
					boolean isBracketOnly = BRACKET_ONLY.matcher(line).find();

					if (hasLiteralText && !line.contains("#{") && !hasThTextAttribute && !isBracketOnly) {
						report.append("HTML: ")
							.append(file)
							.append(" Line ")
							.append(i + 1)
							.append(": ")
							.append(line)
							.append("\n");
					}
				}
			}
		}

		if (!report.isEmpty()) {
			fail("Hardcoded (non-internationalized) strings found:\n" + report);
		}
	}

	@Test
	public void checkI18nPropertyFilesAreInSync() throws IOException {
		List<Path> propertyFiles;
		try (Stream<Path> stream = Files.walk(Paths.get(I18N_DIR))) {
			propertyFiles = stream.filter(p -> p.getFileName().toString().startsWith(BASE_NAME))
				.filter(p -> p.getFileName().toString().endsWith(PROPERTIES))
				.toList();
		}

		Map<String, Properties> localeToProps = new HashMap<>();

		for (Path path : propertyFiles) {
			Properties props = new Properties();
			try (var reader = Files.newBufferedReader(path)) {
				props.load(reader);
				localeToProps.put(path.getFileName().toString(), props);
			}
		}

		String baseFile = BASE_NAME + PROPERTIES;
		Properties baseProps = localeToProps.get(baseFile);
		if (baseProps == null) {
			fail("Base properties file '" + baseFile + "' not found.");
			return;
		}

		Set<String> baseKeys = baseProps.stringPropertyNames();
		StringBuilder report = new StringBuilder();

		for (Map.Entry<String, Properties> entry : localeToProps.entrySet()) {
			String fileName = entry.getKey();
			// We use fallback logic to include english strings, hence messages_en is not
			// populated.
			if (fileName.equals(baseFile) || fileName.equals("messages_en.properties"))
				continue;

			Properties props = entry.getValue();
			Set<String> missingKeys = new TreeSet<>(baseKeys);
			missingKeys.removeAll(props.stringPropertyNames());

			if (!missingKeys.isEmpty()) {
				report.append("Missing keys in ").append(fileName).append(":\n");
				missingKeys.forEach(k -> report.append("  ").append(k).append("\n"));
			}
		}

		if (!report.isEmpty()) {
			fail("Translation files are not in sync:\n" + report);
		}
	}

}
