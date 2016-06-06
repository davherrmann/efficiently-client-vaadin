package de.davherrmann.efficiently;

import static com.google.common.collect.Maps.newHashMap;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;

import de.davherrmann.efficiently.components.Table.TableProperties;
import de.davherrmann.immutable.Immutable.ImmutableNode;

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
