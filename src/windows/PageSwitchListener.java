package windows;

/**
 * Used to detect when the user switches
 * from one Page to another in MainWindow
 * 
 * @author Matt Crow
 */
public interface PageSwitchListener {
    public void leavingPage(NewPage p);
    public void switchedToPage(NewPage p);
}
