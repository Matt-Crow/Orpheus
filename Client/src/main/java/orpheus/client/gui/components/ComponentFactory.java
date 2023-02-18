package orpheus.client.gui.components;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * Uses the factory design pattern to produce the components needed by the GUI
 * @author Matt Crow
 */
public class ComponentFactory {
    private static Color PRIMARY_COLOR = new Color(155, 155, 0); // gold
    
    public JButton makeButton(String text, Runnable onClick){
        JButton button = new JButton(text);
        button.setBackground(PRIMARY_COLOR);
        button.addActionListener((e)->onClick.run());
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.orange);
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
                button.repaint();
            }
        });
        return button;
    }
    
    public JLabel makeLabel(String text){
        JLabel label = new JLabel(text);
        label.setOpaque(true);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }
    
    public JPanel makePanel(){
        JPanel panel = new JPanel();
        
        return panel;
    }
    
    public JPanel makeSpaceAround(JComponent component){
        return makeSpaceAround(component, Color.GRAY);
    }
    
    public JPanel makeSpaceAround(JComponent component, Color spaceColor){
        JPanel p = makePanel();
        
        // center align component: https://stackoverflow.com/a/898054
        p.setLayout(new GridBagLayout());
        p.add(component, new GridBagConstraints());
        
        p.setBackground(spaceColor);
        return p;
    }

    /**
     * Adds a scroll pane around the given component, which will always show a
     * verical scrollbar, but never a horizontal one.
     * 
     * @param component the component to decorate
     * @return the decorated component
     */
    public JScrollPane makeVerticalScrollAround(JComponent component) {
        var scrolly = new JScrollPane(component);
        scrolly.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrolly.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scrolly;
    }
    
    /**
     * 
     * @param text the text to include in the text area
     * @return an un-editable text area 
     */
    public JTextArea makeTextArea(String text){
        JTextArea textArea = new JTextArea(text);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setRows(4);
        return textArea;
    }
    
    /**
     * @return an empty text area
     */
    public JTextArea makeTextArea(){
        return makeTextArea("");
    }
}
