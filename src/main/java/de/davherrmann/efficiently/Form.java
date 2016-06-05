package de.davherrmann.efficiently;

import java.util.List;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class Form implements Element
{
    private final VerticalLayout form = new VerticalLayout();

    public Form()
    {
        form.setSpacing(true);
    }

    @Override
    public void changeContent(final List<Element> content)
    {
        form.removeAllComponents();
        content.stream() //
            .map(Element::component) //
            .forEach(form::addComponent);
    }

    @Override
    public Component component()
    {
        return form;
    }
}
