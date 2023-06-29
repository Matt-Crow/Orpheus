package orpheus.core.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * register prototypes, then clone them later
 */
public class PrototypeFactory<T extends Prototype> {
    
    private final HashMap<String, T> prototypes = new HashMap<>();

    /**
     * Registers a prototype so it can be cloned later
     * @param prototype the prototype to register
     */
    public void add(T prototype) {
        prototypes.put(prototype.getName(), prototype);
    }

    /**
     * Registers all prototypes so they can be cloned later
     * @param prototypes the prototypes to register
     */
    public void addAll(List<T> prototypes) {
        prototypes.forEach(this::add);
    }

    /**
     * @param name the name of the prototype to copy
     * @throws IllegalArgumentException if no prototype with the given name is 
     *  registered
     * @return a copy of the registered prototype with the given name
     */
    @SuppressWarnings("unchecked")
    public T make(String name) {
        if (!prototypes.containsKey(name)) {
            throw new IllegalArgumentException("No prototype registered with name " + name);
        }
        var copy = prototypes.get(name).copy();
        
        return (T)copy;
    }

    /**
     * @return all registered names, sorted alphabetically
     */
    public List<String> getAllNames() {
        var result = new ArrayList<>(prototypes.keySet());
        Collections.sort(result);
        return result;
    }

    /**
     * @return copies of all available prototypes
     */
    public List<T> getAll() {
        return getAllNames()
            .stream()
            .sorted()
            .map(this::make)
            .toList();
    }
}
