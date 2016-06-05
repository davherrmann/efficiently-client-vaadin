package de.davherrmann.efficiently;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;
import java.util.function.Consumer;

import com.vaadin.event.UIEvents;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

import de.davherrmann.efficiently.components.Refresher.RefresherProperties;
import de.davherrmann.efficiently.server.Action;
import de.davherrmann.efficiently.server.Dispatcher;

public class Refresher implements Element, BindingHolder<RefresherProperties>
{
    private final Component dummy = new CssLayout();
    private final Map<String, Consumer<Object>> propertyBindings = newHashMap();
    private final UIEvents.PollListener pollListener;

    private int delay = 1000;

    public Refresher(final Dispatcher dispatcher)
    {
        pollListener = e -> dispatcher.dispatch(new Action("refresh"));

        bind(path()::refresh).to(refresh -> {
            if (refresh)
            {
                dummy.getUI().addPollListener(pollListener);
                dummy.getUI().setPollInterval(delay);
            }
            else
            {
                dummy.getUI().removePollListener(pollListener);
                dummy.getUI().setPollInterval(-1);
            }
        });
        bind(path()::delay).to(delay -> this.delay = delay);
    }

    @Override
    public Map<String, Consumer<Object>> propertyBindings()
    {
        return propertyBindings;
    }

    @Override
    public Component component()
    {
        return dummy;
    }
}
