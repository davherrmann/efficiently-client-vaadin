package de.davherrmann.efficiently;

import com.vaadin.ui.Component;

import de.davherrmann.efficiently.server.Dispatcher;

public class Button implements Element
{
    private final com.vaadin.ui.Button button = new com.vaadin.ui.Button();
    public Button(final Dispatcher dispatcher)
    {

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
}
