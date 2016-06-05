package de.davherrmann.efficiently;

import java.util.List;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

public class FormGroup implements Element
{
    private final HorizontalLayout formGroup = new HorizontalLayout();

    public FormGroup()
    {
        formGroup.setSpacing(true);
    }

    @Override
    public void changeContent(final List<Element> content)
    {
        formGroup.removeAllComponents();
        content.stream() //
            .map(Element::component) //
            .forEach(formGroup::addComponent);
    }

    @Override
    public Component component()
    {
        return formGroup;
    }
}
