package de.davherrmann.efficiently;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;

import de.davherrmann.efficiently.server.Action;
import de.davherrmann.efficiently.server.Dispatcher;

public class Button implements Element, Bindable
{
    private final com.vaadin.ui.Button button = new com.vaadin.ui.Button();
    private final Dispatcher dispatcher;

    public Button(final Dispatcher dispatcher)
    {
        this.dispatcher = dispatcher;
    }

    @Override
    public void changeContent(final String content)
    {
        button.setCaption(content);
    }

    @Override
    public Component component()
    {
        return button;
    }

    @Override
    public void changeProperty(final String property, final Object value)
    {
        if ("onClick".equals(property))
        {
            button.removeListener(ClickEvent.class, button);
            button.addClickListener(e -> dispatcher.dispatch(new Action((String) value)));
        }
    }
}
