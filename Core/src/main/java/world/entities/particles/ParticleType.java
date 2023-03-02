package world.entities.particles;

import java.util.Arrays;

/**
 * Used by projectiles to determine the shape they should spawn particles in.
 */
public enum ParticleType {

    /**
     * emit particles in all directions
     */
	BURST("burst"), 

    /**
     * emit particles behind
     */
    BEAM("beam"),

    /**
     * emit particles in a wake
     */
    SHEAR("shear"),

    /**
     * emit immobile particles at your location
     */
    BLADE("blade"), 

    /**
     * emit no particles
     */
    NONE("none");
    
    private final String name;
    
    ParticleType(String n){
        name = n;
    }
    
    public static ParticleType fromString(String s){
        return Arrays.stream(values())
            .filter((pt) -> pt.name.equals(s))
            .findFirst()
            .get();
    }
    
    @Override
    public String toString(){
        return name;
    }
};
