package orpheus.client.gui.components;

import java.awt.Color;
import java.awt.Component;
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
    
    public JLabel makeLabel(String text){
        JLabel label = new JLabel(text);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }
    
    public JButton makeButton(String text, Runnable onClick){
        JButton button = new JButton(text);
        button.addActionListener((e)->onClick.run());
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.orange);
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new JButton().getBackground());
                button.repaint();
            }
        });
        return button;
    }
    
    public JPanel makeSpaceAround(JComponent component, Color spaceColor){
        JPanel p = new JPanel();
        
        // center align component: https://stackoverflow.com/a/898054
        p.setLayout(new GridBagLayout());
        p.add(component, new GridBagConstraints());
        
        p.setBackground(spaceColor);
        return p;
    }
}
