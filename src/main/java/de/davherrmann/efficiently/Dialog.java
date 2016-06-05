package de.davherrmann.efficiently;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;
import java.util.function.Consumer;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

import de.davherrmann.efficiently.components.Dialog.DialogProperties;

public class Dialog implements Element, BindingHolder<DialogProperties>
{
    private final Component dummy = new CssLayout();
    private final Map<String, Consumer<Object>> propertyBindings = newHashMap();

    @Override
    public Map<String, Consumer<Object>> propertyBindings()
    {
        return propertyBindings;
    }

    @Override
    public Component component()
    {
        return dummy;
    }
}
