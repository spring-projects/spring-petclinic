package org.springframework.samples.petclinic;

import java.io.File;
import java.io.FileNotFoundException;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;


import org.springframework.samples.petclinic.ImpactAnalyzer.connect.Connect;
import org.springframework.samples.petclinic.ImpactAnalyzer.models.HTMLJSONFileParser;
import org.springframework.samples.petclinic.ImpactAnalyzer.models.TestFileParser;

public class ImpactAnalyzerTest {
	private static final String FILE_PATH = "./src/test/java/SeleniumTest.java";
	private static final String JSON_PATH = "./json/aHR0cDovL2xvY2FsaG9zdDo4MDgwL293bmVycy8xL3BldHMvbmV3.json";

	@org.junit.Test
	public void Test() throws FileNotFoundException {
		Connect.connect();
		try {
			HTMLJSONFileParser htmlJsonFileParser = new HTMLJSONFileParser(JSON_PATH);
			htmlJsonFileParser.setElements();
			var htmlElements = htmlJsonFileParser.getHTMLElements();

			CompilationUnit testCompUnit = StaticJavaParser.parse(new File(FILE_PATH));

			var testElements = TestFileParser.parse(FILE_PATH);



			DependencyAnalyzer dependencyAnalyzer = new DependencyAnalyzer();
			dependencyAnalyzer.dependencyAnalyzer(htmlElements, testElements);
		}catch(Exception e) {}
		Connect.close();
	}
}
