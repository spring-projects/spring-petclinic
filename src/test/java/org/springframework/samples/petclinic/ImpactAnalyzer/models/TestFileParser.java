package org.springframework.samples.petclinic.ImpactAnalyzer.models;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public final class TestFileParser {

    private TestFileParser() {
    }

    public final static ArrayList<TestElement> parse(String testPath)
            throws URISyntaxException, IOException, ParseException {
        var compUnit = StaticJavaParser.parse(new File(testPath));
        return TestFileParser.parse(compUnit);
    }

    public final static ArrayList<TestElement> parse(File file) throws URISyntaxException, IOException, ParseException {
        var compUnit = StaticJavaParser.parse(file);
        return TestFileParser.parse(compUnit);
    }

    public final static ArrayList<TestElement> parse(CompilationUnit compUnit)
            throws URISyntaxException, IOException, ParseException {
        var testElements = new ArrayList<TestElement>();
        var testDriverNames = new ArrayList<String>();
        for (var testClass : compUnit.findAll(ClassOrInterfaceDeclaration.class)) {
            for (var variable : testClass.findAll(VariableDeclarator.class)) {
                if (variable.getTypeAsString().equals("WebDriver"))
                    testDriverNames.add(variable.toString());
            }
            for (var test : testClass.findAll(MethodDeclaration.class)) {
                var previousUrl = new String();
                var currentUrl = new String();
                var host = new String();
                var hrefElement = false;
                var methodCallExprs = new ArrayList<MethodCallExpr>();
                for (var variable : test.findAll(VariableDeclarator.class)) {
                    if (variable.getTypeAsString().equals("WebDriver"))
                        testDriverNames.add(variable.toString());
                }
                test.accept(new VoidVisitorAdapter<Void>() {
                    @Override
                    public void visit(MethodCallExpr call, Void arg) {
                        methodCallExprs.add(call);
                        super.visit(call, arg);
                    }
                }, null);
                for (var methodCallExpr : methodCallExprs) {
                    var methodName = methodCallExpr.getNameAsString();
                    var callerName = methodCallExpr.getScope().get().toString();
                    if (methodName.equals("findElement")) {
                        var arg = methodCallExpr.getArgument(0).asMethodCallExpr();
                        var accessMethod = arg.getNameAsString();
                        var accessValue = deleteStrtEndQuotes(arg.getArgument(0).toString());
                        var startingLine = arg.getArgument(0).getBegin().get().line;
                        var startingColumn = arg.getArgument(0).getBegin().get().column;
                        var startingPosition = new Position(startingLine, startingColumn);
                        var url = new String();
                        if (!hrefElement)
                            url = currentUrl;
                        else {
                            url = previousUrl;
                            hrefElement = false;
                        }
                        testElements.add(new TestElement(testClass.toString(), test.toString(), url, callerName,
                                startingPosition, accessMethod, accessValue));
                    } else if (methodName.equals("get") && testDriverNames.contains(callerName)) {
                        previousUrl = currentUrl;
                        currentUrl = deleteStrtEndQuotes(methodCallExpr.getArgument(0).toString());
                        host = new URI(currentUrl).getHost();
                        HtmlParser.parseToJsonFile(currentUrl);
                    } else if (methodName.equals("click")) {
                        var accessMethodTmp = new ArrayList<String>();
                        var valueTmp = new ArrayList<String>();
                        methodCallExpr.accept(new VoidVisitorAdapter<Void>() {
                            @Override
                            public void visit(MethodCallExpr call, Void arg) {
                                if (call.getChildNodes().get(0).toString().equals("By")) {
                                    accessMethodTmp.add(call.getChildNodes().get(1).toString());
                                    valueTmp.add(call.getChildNodes().get(2).toString());
                                }
                                super.visit(call, arg);
                            }
                        }, null);
                        var accessMethod = deleteStrtEndQuotes(accessMethodTmp.get(0));
                        var value = deleteStrtEndQuotes(valueTmp.get(0));
                        var encodedUrl = Base64.getEncoder().encodeToString(currentUrl.getBytes("utf-8"));
                        var json = new File("json/" + encodedUrl + ".json");
                        var reader = new FileReader(json);
                        var jsonParser = new JSONParser();
                        var jsonArray = (JSONArray) jsonParser.parse(reader);
                        for (var object : jsonArray) {
                            var jsonObject = (JSONObject) object;
                            if (jsonObject.get(accessMethod).equals(value)) {
                                hrefElement = true;
                                previousUrl = currentUrl;
                                var href = (String) jsonObject.get("href");
                                if (href.startsWith("/"))
                                    currentUrl = host + href;
                                else if( !(href.equals(""))) {
                                    currentUrl = href;
                                    host = new URI(currentUrl).getHost();
                                }
                                HtmlParser.parseToJsonFile(currentUrl);
                            }
                        }
                    }
                }
            }
        }
        return testElements;
    }

    private final static String deleteStrtEndQuotes(String str) {
        if (str.startsWith("\"") && str.endsWith("\""))
            return str.substring(1, str.length() - 1);
        return str;
    }

}
