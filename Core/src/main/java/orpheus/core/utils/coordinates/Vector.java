package orpheus.core.utils.coordinates;

public interface Vector {
    
    public double getVelocityX();

    public double getVelocityY();

    public default double getSpeed() {
        var dx = getVelocityX();
        var dy = getVelocityY();
        return Math.sqrt(dx*dx + dy*dy);
    }
}
