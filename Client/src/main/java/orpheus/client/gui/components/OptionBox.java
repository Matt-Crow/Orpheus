package orpheus.client.gui.components;

import javax.swing.*;

import java.util.HashMap;

public class OptionBox<T> extends JComponent {

    private final HashMap<String, T> options;
    private final JComboBox<String> box;

    public OptionBox(ComponentFactory cf, String t, T[] opt) {
        super();
        
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        add(cf.makeLabel(t));

        box = new JComboBox<>();
        add(box);

        options = new HashMap<>();
        for (T opt1 : opt) {
            addOption(opt1);
        }
    }

    public void clear() {
        box.removeAllItems();
        options.clear();
    }

    public void addOption(T opt) {
        options.put(opt.toString(), opt);
        box.addItem(opt.toString());
    }

    public void setSelected(T item) {
        if (options.containsKey(item.toString())) {
            box.setSelectedItem(item.toString());
        } else {
            throw new IllegalArgumentException("Item not found: " + item);
        }
    }

    public void setSelectedIndex(int i) {
        if (i < 0 || i >= box.getItemCount()) {
            throw new IndexOutOfBoundsException();
        }
        box.setSelectedIndex(i);
    }

    public void setSelectedName(String s) {
        if (options.containsKey(s)) {
            box.setSelectedItem(s);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public T getSelected() {
        T ret = null;
        String name = (String) box.getSelectedItem();
        if (name != null && options.containsKey(name)) {
            ret = options.get(name);
        }
        return ret;
    }

    public void addActionListener(AbstractAction a) {
        box.addActionListener(a);
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        box.setEnabled(b);
    }
}
