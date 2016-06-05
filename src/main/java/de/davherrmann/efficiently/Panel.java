package de.davherrmann.efficiently;

import static com.google.common.collect.Maps.newHashMap;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import de.davherrmann.efficiently.components.Element.ElementProperties;

public class Panel implements Element, BindingHolder<ElementProperties>
{
    private final VerticalLayout panel = new VerticalLayout();
    private final Map<String, Consumer<Object>> propertyBindings = newHashMap();

    @Override
    public Map<String, Consumer<Object>> propertyBindings()
    {
        return propertyBindings;
    }

    @Override
    public void changeContent(final List<Element> content)
    {
        panel.removeAllComponents();
        content.stream() //
            .map(Element::component) //
            .forEach(panel::addComponent);
    }

    @Override
    public Component component()
    {
        return panel;
    }
}
