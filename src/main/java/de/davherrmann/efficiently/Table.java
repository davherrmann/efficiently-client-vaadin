package de.davherrmann.efficiently;

import static com.google.common.collect.Maps.newHashMap;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.Renderer;

import de.davherrmann.efficiently.components.Table.TableProperties;
import de.davherrmann.immutable.Immutable.ImmutableNode;
import elemental.json.JsonValue;

public class Table implements Element, BindingHolder<TableProperties>
{
    private Map<String, Consumer<Object>> propertyBindings = newHashMap();
    private Grid grid = new Grid();

    public Table()
    {
        grid.setSizeFull();

        bind(path()::columns).to(columns -> {
            grid.removeAllColumns();
            columns.forEach(column -> {
                final Grid.Column gridColumn = grid.addColumn(column.name());
                gridColumn.setWidth(column.width() > 0
                    ? column.width()
                    : 200);

                final Renderer<?> renderer = rendererFor(column.renderer());
                if (renderer != null)
                {
                    gridColumn.setRenderer(renderer);
                }
            });
        });
        bind(path()::items).to(items -> items.forEach(item -> {
            final List<Object> row = grid.getColumns().stream() //
                .map(Grid.Column::getPropertyId) //
                .map(id -> propertyFrom(item, id.toString())) //
                .collect(toList());
            grid.addRow(row.toArray());
        }));
    }

    private Renderer<?> rendererFor(final String renderer)
    {
        if ("imageByUrlRenderer".equals(renderer))
        {
            return new HtmlRenderer()
            {
                @Override
                public JsonValue encode(final String value)
                {
                    return super.encode("<img src=\"" + value + "\" />");
                }
            };
        }
        return null;
    }

    public Object propertyFrom(final Object item, final String id)
    {
        return ((ImmutableNode) item).values().get(id);
    }

    @Override
    public Map<String, Consumer<Object>> propertyBindings()
    {
        return propertyBindings;
    }

    @Override
    public Component component()
    {
        return grid;
    }
}
