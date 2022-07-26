package org.springframework.samples.petclinic.ImpactAnalyzer.models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.GsonBuilder;

public final class HtmlParser {

    private HtmlParser() {
    }

    public final static void parseToJsonFile(Object src) throws IOException {
        var folderPath = "json/";
        var extention = ".json";
        var encodedSrc = switch (src) {
            case null, default -> throw new IllegalArgumentException();
            case String s -> Base64.getEncoder().encodeToString(s.getBytes("utf-8"));
            case File f -> Base64.getEncoder().encodeToString(f.getPath().getBytes("utf-8"));
        };
        parseToJsonFile(src, folderPath + encodedSrc + extention);
    }

    public final static void parseToJsonFile(Object src, Object out) throws IOException {
        var file = switch (out) {
            case null, default -> throw new IllegalStateException();
            case String s -> new FileWriter(s);
            case File f -> new FileWriter(f);
        };
        ArrayList<JSONObject> objects;
        switch (src) {
            case null:
            case default:
                file.close();
                throw new IllegalStateException();
            case String s:
                objects = parseToJsonObjects(s);
                break;
            case File f:
                objects = parseToJsonObjects(f);
                break;
            case Document d:
                objects = parseToJsonObjects(d);
                break;
            case Elements e:
                objects = parseToJsonObjects(e);
                break;
        };

        var gson = new GsonBuilder().setPrettyPrinting().create();
        file.write(gson.toJson(objects));
        file.close();
    }

    public final static ArrayList<JSONObject> parseToJsonObjects(String url) throws IOException {
        var doc = Jsoup.connect(url).get();
        return parseToJsonObjects(doc);
    }

    public final static ArrayList<JSONObject> parseToJsonObjects(File file) throws IOException {
        var doc = Jsoup.parse(file, "UTF-8", "");
        return parseToJsonObjects(doc);
    }

    public final static ArrayList<JSONObject> parseToJsonObjects(Document doc) {
        var elems = doc.getAllElements();
        return parseToJsonObjects(elems);
    }

    public final static ArrayList<JSONObject> parseToJsonObjects(Elements elems) {
        if (elems.get(0).tagName().equals("#root"))
            elems.remove(0);
        var elemToXpath = getElementToXpath(elems);
        var jsonObjects = new ArrayList<JSONObject>();
        for (var elem : elems) {
            var item = new LinkedHashMap<String, Object>();
            item.put("tag", elem.tagName());
            item.put("xpath", elemToXpath.get(elem));
            item.put("id", elem.id());
            item.put("class", elem.className());
            item.put("cssSelector", elem.cssSelector());
            item.put("name", elem.attr("name"));
            item.put("href", elem.attr("href"));
            jsonObjects.add(new JSONObject(item));
        }
        return jsonObjects;
    }

    private final static LinkedHashMap<Element, String> getElementToXpath(Elements elems) {
        var elemToXpath = new LinkedHashMap<Element, String>();
        initializePath(elemToXpath, elems);
        indexPath(elemToXpath, elems);
        return elemToXpath;
    }

    private final static void initializePath(LinkedHashMap<Element, String> elemToXpath, Elements elems) {
        for (var elem : elems) {
            elemToXpath.put(elem, "/" + elem.tagName());
        }
    }

    private final static void indexPath(LinkedHashMap<Element, String> elemToXpath, Elements elems) {
        for (var elem : elems) {
            var tagToCount = new HashMap<String, Integer>();
            var children = elem.children();
            var xpath = elemToXpath.get(elem);
            for (var child : children) {
                tagToCount.put(child.tagName(), tagToCount.getOrDefault(child.tagName(), 0) + 1);
            }
            tagToCount.keySet().removeAll(
                    tagToCount.entrySet().stream()
                            .filter(a -> a.getValue() <= 1)
                            .map(e -> e.getKey()).collect(Collectors.toList()));
            for (var j = children.size() - 1; j >= 0; j--) {
                var child = children.get(j);
                var tagCount = tagToCount.getOrDefault(child.tagName(), 0);
                if (tagCount != 0) {
                    elemToXpath.put(child, xpath + "/" + child.tagName() + "[" + tagCount + "]");
                    tagToCount.put(child.tagName(), tagToCount.getOrDefault(child.tagName(), 0) - 1);
                } else {
                    elemToXpath.put(child, xpath + "/" + child.tagName());
                }
            }
        }
    }

}
