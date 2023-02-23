package orpheus.core.world.graph;

/**
 * denotes that an object can be converted to a GraphElement
 */
public interface Graphable {
    
    /**
     * Creates a simplified version of this object that can be sent to remote
     * clients, deserialized, and rendered.
     * @return a graph representation of this object
     */
    public GraphElement toGraph();
}
