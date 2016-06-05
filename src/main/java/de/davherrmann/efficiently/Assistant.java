package de.davherrmann.efficiently;

import static com.google.common.collect.Maps.newHashMap;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.davherrmann.efficiently.components.Assistant.AssistantProperties;
import de.davherrmann.efficiently.server.Action;
import de.davherrmann.efficiently.server.Dispatcher;

public class Assistant implements Element, BindingHolder<AssistantProperties>
{
    private final VerticalLayout assistantContent = new VerticalLayout();
    private final VerticalLayout assistant;
    private final Map<String, Consumer<Object>> propertyBindings = newHashMap();
    private final Button previous;
    private final Button next;
    private List<Component> children;

    public Assistant(final Dispatcher dispatcher)
    {
        final Label title = new Label("title");
        final HorizontalLayout actionsLayout = new HorizontalLayout( //
            new com.vaadin.ui.Button("print"), //
            new Button("save"), //
            new Button("close"));

        previous = new Button("previous", (e) -> dispatcher.dispatch(new Action("assistantAction/previous")));
        next = new Button("next", (e) -> dispatcher.dispatch(new Action("assistantAction/next")));
        assistant = new VerticalLayout( //
            actionsLayout, //
            new VerticalLayout( //
                title, //
                assistantContent), //
            new HorizontalLayout( //
                previous, //
                next));

        bind(path()::title).to(title::setValue);
        bind(path()::actions).to(actions -> {
            actionsLayout.removeAllComponents();
            actions.stream().forEach(action -> {
                final Button button = new Button(action);
                button.addClickListener(e -> dispatcher.dispatch(new Action("assistantAction/" + action)));
                actionsLayout.addComponent(button);
            });
        });
        bind(path()::currentPage).to(currentPage -> {
            next.setEnabled(currentPage < children.size() - 1);
            previous.setEnabled(currentPage > 0);
            range(0, children.size()) //
                .forEach(id -> children.get(id).setVisible(id == currentPage));
        });
    }

    @Override
    public void changeContent(final List<Element> content)
    {
        assistantContent.removeAllComponents();
        children = content.stream() //
            .map(Element::component) //
            .collect(toList());
        children.forEach(assistantContent::addComponent);
    }

    @Override
    public Map<String, Consumer<Object>> propertyBindings()
    {
        return propertyBindings;
    }

    @Override
    public Component component()
    {
        return assistant;
    }
}
