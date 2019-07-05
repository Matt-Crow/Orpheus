package serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used by SafeList to check whether
 * or not to serialize a node.
 * This is used to reduce the lag
 * caused by serializing Particles,
 * as they can easily be replicated 
 * by the client, and have no impact
 * on the game if they are not present
 * @author Matt Crow
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NoSerialize {
    
}
