package de.davherrmann.efficiently;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

public class NotImplemented implements Element, Bindable
{
    private final Map<String, Object> properties = new HashMap<>();
    private final String type;

    public NotImplemented(final String type)
    {
        this.type = type;
    }

    @Override
    public void changeProperty(final String property, final Object value)
    {
        properties.put(property, value);
    }

    @Override
    public Component component()
    {
        return new Label("not implemented: <" + type + ">" + new Gson().toJson(properties));
    }
}
