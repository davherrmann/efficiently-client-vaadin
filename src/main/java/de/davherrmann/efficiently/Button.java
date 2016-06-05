package de.davherrmann.efficiently;

import com.vaadin.ui.Component;

public class Button implements Element
{
    private final com.vaadin.ui.Button button = new com.vaadin.ui.Button();

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
