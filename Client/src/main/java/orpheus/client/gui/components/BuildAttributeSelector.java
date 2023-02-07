package orpheus.client.gui.components;

import java.awt.BorderLayout;
import java.util.HashMap;
import javax.swing.*;

import world.build.AbstractBuildAttribute;

/**
 * presents a component where the user can select an AbstractBuildAttribute, 
 * such as a CharacterClass, Active, or Passive
 */
public class BuildAttributeSelector<T extends AbstractBuildAttribute> extends JComponent {

    /**
     * presents a list of AbstractBuildAttribute names
     */
    private final JComboBox<String> chooser;

    /**
     * maps choices in the chooser to AbstractBuildAttributes
     */
    private final HashMap<String, T> options;

    public BuildAttributeSelector(ComponentFactory cf, String title, T[] a) {
        super();
        options = new HashMap<>();

        setLayout(new BorderLayout());

        add(cf.makeLabel(title), BorderLayout.PAGE_START);

        var desc = cf.makeTextArea();
        var scrolly = new JScrollPane(desc);
        scrolly.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrolly.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrolly, BorderLayout.CENTER);

        chooser = new JComboBox<>();
        chooser.addActionListener((e) -> {
            if (getSelected() != null) {
                desc.setText(getSelected().getDescription());
                SwingUtilities.invokeLater(() -> {
                    scrolly.getVerticalScrollBar().setValue(0);
                });
            }
        });
        add(chooser, BorderLayout.PAGE_END);

        setOptions(a);
    }

    /**
     * replaces all the options this currently has with the new ones
     * @param acs the new build attribute the user can select
     */
    public void setOptions(T[] acs) {
        chooser.removeAllItems();
        options.clear();
        for (T ac : acs) {
            addOption(ac);
        }
    }

    private void addOption(T ac) {
        if (options.containsKey(ac.getName())) {
            throw new IllegalArgumentException(ac.getName() + " is already an option.");
        }
        options.put(ac.getName(), ac);
        chooser.addItem(ac.getName());
    }

    /**
     * chooses the build attribute with the same name as the given one
     * @param ac the build attribute to simulate selecting
     * @throws IllegalArgumentException if no attribute with the given one's 
     *  name is an option
     */
    public void setSelected(T ac) {
        if (!options.containsKey(ac.getName())) {
            throw new IllegalArgumentException(ac.getName() + " is not a valid option");
        }
        chooser.setSelectedItem(ac.getName());
    }

    /**
     * @return the build attribute the user selected, or null if no attribute is
     *  selected
     */
    public T getSelected() {
        T ret = null;
        /*
        JComboBox::getSelectedItem returns Object, but this method should throw
        an exception if the selected item is not a String for some reason
         */
        if (options.containsKey((String) chooser.getSelectedItem())) {
            ret = options.get((String) chooser.getSelectedItem());
        }
        return ret;
    }
}
