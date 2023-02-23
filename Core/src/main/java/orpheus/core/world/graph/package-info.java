package orpheus.core.world.graph;
/**
 * For want of a better term, a graph is the simplified version of a game world
 * which is sent to clients in multiplayer and rendered by the screen.
 * 
 * The mapping of a world to a graph is one way, as a graph only contains
 * fields required to draw the object and understand what operations can be
 * performed on it.
 * 
 * Using a graph instead of the full object has two primary benefits: first, it
 * avoids the security complications of serialization; second, it reduces the
 * size of objects transmitted to clients.
 */