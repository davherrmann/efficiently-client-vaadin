package de.davherrmann.efficiently;

import static com.google.common.collect.Maps.newHashMap;
import static de.davherrmann.immutable.PathRecorder.pathInstanceFor;
import static java.util.Arrays.asList;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

import de.davherrmann.efficiently.components.Field.FieldProperties;

public class Field implements Element, BindingHolder<FieldProperties>
{
    private final TextField field = new TextField();
    private final Map<String, Consumer<Object>> propertyBindings = newHashMap();

    public Field()
    {
        final FieldProperties properties = pathInstanceFor(propertyType());
        bind(properties::label).to(field::setCaption);
        bind(properties::cols).to(value -> {
            final List<String> cols = asList(value.split("."));
            if (cols.size() < 2)
            {
                return;
            }
            field.setWidth(cols.get(1));
        });
        bind(properties::value).to(field::setValue);
    }

    @Override
    public Map<String, Consumer<Object>> propertyBindings()
    {
        return propertyBindings;
    }

    @Override
    public Component component()
    {
        return field;
    }
}
