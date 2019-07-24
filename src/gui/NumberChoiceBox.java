package gui;

import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author Matt
 */
public class NumberChoiceBox extends JComponent{
    private final ArrayList<Consumer<Integer>> selectionChangedListeners;
    private final HashMap<Integer, JRadioButton> buttons;
    private int selected;
    
    public NumberChoiceBox(String text, int min, int max){
        if(min > max){
            throw new IllegalArgumentException("Minimum must be less than or equal to maximum");
        }
        
        selectionChangedListeners = new ArrayList<>();
        buttons = new HashMap<>();
        
        setLayout(new GridLayout(2, 1));
        Style.applyStyling(this);
        
        JLabel label = new JLabel(text);
        Style.applyStyling(label);
        add(label);
        
        JPanel radButtons = new JPanel();
        Style.applyStyling(radButtons);
        add(radButtons);
        ButtonGroup bg = new ButtonGroup();
        for(int i = min; i <= max; i++){
            JRadioButton rad = new JRadioButton("" + i);
            rad.addItemListener((ItemEvent e)->{
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    fireSelectionChanged(Integer.parseInt(rad.getText()));
                }
            });
            bg.add(rad);
            radButtons.add(rad);
            buttons.put(i, rad);
        }
        radButtons.setLayout(new GridLayout(1, max - min + 1));
        setSelected(min);
    }
    
    public void setSelected(int i){
        if(buttons.containsKey(i)){
            buttons.get(i).setSelected(true);
        } else {
            throw new IllegalArgumentException("Invalid option: " + i);
        }
    }
    public int getSelected(){
        return selected;
    }
    
    private void fireSelectionChanged(int toVal){
        selected = toVal;
        selectionChangedListeners.stream().forEach(c->c.accept(toVal));
    }
    
    public void addSelectionChangeListener(Consumer<Integer> listener){
        selectionChangedListeners.add(listener);
    }
}
