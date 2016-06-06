package de.davherrmann.efficiently;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;

import de.davherrmann.efficiently.app.MySpecialState;
import de.davherrmann.efficiently.server.Dispatcher;
import de.davherrmann.immutable.Immutable;

public class ViewRenderer
{
    private final List<Binding> bindings = newArrayList();
    private final Dispatcher dispatcher;

    public ViewRenderer(final Dispatcher dispatcher)
    {
        this.dispatcher = dispatcher;
    }

    public Element createComponentFrom(Map<String, Object> template)
    {
        final Element element = createElement(typeOf(template));
        final Object content = template.get("content");

        if (content instanceof String)
        {
            bindings.add(new Binding((String) content, element, "content"));
        }
        else if (content instanceof List)
        {
            @SuppressWarnings("unchecked")
            final List<Map<String, Object>> contentList = (List<Map<String, Object>>) content;
            element.changeContent(contentList.stream() //
                .map(this::createComponentFrom) //
                .collect(toList()));
        }

        // put source -> type into binding map
        template.entrySet().stream() //
            .filter(e -> !("type".equals(e.getKey()) || "content".equals(e.getKey()))) //
            .forEach(e -> bindings.add(new Binding(sourceFromDerivation(e.getValue()), element, e.getKey())));

        return element;
    }

    @SuppressWarnings("unchecked")
    private String sourceFromDerivation(final Object value)
    {
        return ((Map<String, String>) value).get("sourceValue");
    }

    private Element createElement(String type)
    {
        switch (type)
        {
            case "Panel":
                return new Panel();
            case "Assistant":
                return new Assistant(dispatcher);
            case "Form":
                return new Form();
            case "FormGroup":
                return new FormGroup();
            case "Field":
                return new Field();
            case "Dialog":
                return new Dialog(dispatcher);
            case "Refresher":
                return new Refresher(dispatcher);
            case "Button":
                return new Button(dispatcher);
            case "default":
            default:
                return new NotImplemented(type);
        }
    }

    private String typeOf(Map<String, Object> template)
    {
        return template.getOrDefault("type", new DefaultType()).toString();
    }

    public void init()
    {
        bindings.clear();
    }

    public void update(final Immutable<MySpecialState> diff)
    {
        diff.visitNodes((path, value) -> bindingsFor(path).stream() //
            .forEach(binding -> {
                final Element element = binding.element();
                final String property = binding.property();
                if ("content".equals(property))
                {
                    element.changeContent((String) value);
                }
                else if (element instanceof Bindable)
                {
                    ((Bindable) element).changeProperty(property, value);
                }
                else
                {
                    System.out.println(
                        format("Could not change property %s in %s to %s, not a Bindable", property, element, value));
                }
            }));
    }

    private List<Binding> bindingsFor(final String path)
    {
        return bindings.stream() //
            .filter(b -> path.equals(b.source())) //
            .collect(toList());
    }

    private class DefaultType
    {
        @Override
        public String toString()
        {
            return "default";
        }
    }
}
