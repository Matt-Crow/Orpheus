package actions;

/**
 *
 * @author Matt Crow
 */
public interface Terminable {
    public void addTerminationListener(TerminateListener listen);
    public boolean removeTerminationListener(TerminateListener listen);
    public void terminate();
}
