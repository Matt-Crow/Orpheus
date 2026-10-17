package orpheus.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

public class OrpheusJsonHelper {
    public static JsonObject fromString(String s) {
        var json = Json.createReader(new StringReader(s))
            .readObject();
        return json;
    }

    public static JsonArray toJsonArrayOfStrings(List<String> items) {
        return toJsonArray(items, (item, jab) -> jab.add(item));
    }

    public static JsonArray toJsonArrayOfObjects(List<JsonObject> items) {
        return toJsonArray(items, (item, jab) -> jab.add(item));
    }

    private static <T> JsonArray toJsonArray(Collection<T> items, BiConsumer<T, JsonArrayBuilder> each) {
        var array = Json.createArrayBuilder();
        for (var item : items) {
            each.accept(item, array);
        }
        return array.build();
    }

    public static <T> List<T> toListOfObjects(JsonArray array, Function<JsonObject, T> mapper) {
        return toList(array, (a, i) -> mapper.apply(a.getJsonObject(i)));
    }

    public static List<String> toListOfStrings(JsonArray array) {
        return toList(array, (a, i) -> a.getString(i));
    }

    private static <T> List<T> toList(JsonArray array, BiFunction<JsonArray, Integer, T> mapper) {
        var items = new ArrayList<T>();
        for (var i = 0; i < array.size(); i++) {
            items.add(mapper.apply(array, i));
        }
        return items;
    }

    public static JsonObject readFile(File file) throws Exception {
        try (var stream = new FileInputStream(file)) {
            var json = Json.createReader(stream).readObject();
            return json;
        }
    }

    public static void writeFile(File file, JsonObject json) throws Exception {
        try (var stream = new FileOutputStream(file)) {
            Json.createWriter(stream).writeObject(json);
        }
    }
}
