package orpheus.client.gui.components;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Decorator design pattern to add show / hide functionality to a component
 */
public class ShowHideDecorator extends JComponent {
    
    /**
     * A button which, when pressed, either shows or hides the contents of this
     * component.
     */
    private final JButton toggleButton;

    /**
     * The inner component this is decorating
     */
    private final JComponent body;

    public ShowHideDecorator(ComponentFactory componentFactory, JComponent body) {
        setLayout(new BorderLayout());

        toggleButton = componentFactory.makeButton("X", this::toggle);
        var top = new JPanel();
        top.setLayout(new BorderLayout());
        top.add(toggleButton, BorderLayout.LINE_END);
        add(top, BorderLayout.PAGE_START);

        this.body = body;
        add(body, BorderLayout.CENTER);
    }

    private void toggle() {
        if (body.isVisible()) {
            hideBody();
        } else {
            showBody();
        }
    }

    public void hideBody() {
        toggleButton.setText("O");
        body.setVisible(false);
    }

    public void showBody() {
        toggleButton.setText("X");
        body.setVisible(true);
    }
}
