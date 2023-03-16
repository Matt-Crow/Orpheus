package orpheus.core.world.graph.utils;

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

import orpheus.core.world.graph.GraphElement;

public class JsonArrayHelper {

    public static <T> JsonArray toJsonArray(Collection<T> items, BiConsumer<T, JsonArrayBuilder> each) {
        var array = Json.createArrayBuilder();
        for (var item : items) {
            each.accept(item, array);
        }
        return array.build();
    }

    public static JsonArray fromStrings(Collection<String> items) {
        return toJsonArray(items, (item, array) -> array.add(item));
    }

    public static <T extends GraphElement> JsonArray fromGraphElements(Collection<T> items) {
        return toJsonArray(items, (item, array) -> array.add(item.toJson()));
    }

    public static <T> List<T> toList(JsonArray array, BiFunction<JsonArray, Integer, T> mapper) {
        var items = new ArrayList<T>();
        for (var i = 0; i < array.size(); i++) {
            items.add(mapper.apply(array, i));
        }
        return items;
    }

    public static <T> List<T> toGraphElements(JsonArray array, Function<JsonObject, T> mapper) {
        return toList(array, (a, i) -> mapper.apply(a.getJsonObject(i)));
    }

    public static List<String> toStrings(JsonArray array) {
        return toList(array, (a, i) -> a.getString(i));
    }
}
