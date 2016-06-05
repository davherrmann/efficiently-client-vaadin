package de.davherrmann.efficiently;

import static java.lang.String.format;

import java.util.List;

import com.vaadin.ui.Component;

public interface Element
{
    default void changeContent(List<Element> content)
    {
        System.out.println(
            format("Cannot set content (List<Element>) in %s. Please implement changeContent(List<Element> content)",
                this.getClass()));
    }

    default void changeContent(String content)
    {
        System.out.println(format("Cannot set content (String) in %s. Please implement changeContent(String content)",
            this.getClass()));
    }

    Component component();
}
