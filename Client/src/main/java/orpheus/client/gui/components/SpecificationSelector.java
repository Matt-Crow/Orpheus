package orpheus.client.gui.components;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import orpheus.core.champions.Specification;

public class SpecificationSelector<T extends Specification> extends JComponent {
    
    private final HashMap<String, T> options;
    private final JComboBox<String> box;
    private final JTextArea description;
    private final JScrollPane scrolly;

    public SpecificationSelector(
            ComponentFactory cf, 
            Collection<T> choices
    ) {
        options = new HashMap<>();
        for (var choice : choices) {
            options.put(choice.getName(), choice);
        }

        setLayout(new BorderLayout());

        add(cf.makeLabel("Select specification"), BorderLayout.PAGE_START);

        description = cf.makeTextArea();
        description.setColumns(80);
        description.setTabSize(4);

        scrolly = new JScrollPane(description);
        scrolly.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrolly.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrolly, BorderLayout.CENTER);

        box = new JComboBox<>();
        var sortedOptions = new ArrayList<>(options.keySet());
        Collections.sort(sortedOptions);
        for (var option : sortedOptions) {
            box.addItem(option);
        }
        box.addItemListener((e) -> selectionChanged());
        add(box, BorderLayout.PAGE_END);

        selectionChanged();
    }

    private void selectionChanged() {
        var selection = getSelected();
        if (selection.isPresent()) {
            setDescription(selection.get().getDescription());
        }
    }

    private void setDescription(String description) {
        this.description.setText(description);
        SwingUtilities.invokeLater(()->{
            scrolly.getVerticalScrollBar().setValue(0);
            repaint();
        });
    }

    public Optional<T> getSelected() {
        var name = box.getSelectedItem(); // returns Object for some reason
        if (name == null) {
            return Optional.empty();
        }
        var selected = options.get(name.toString());
        return Optional.of(selected);
    }
}
