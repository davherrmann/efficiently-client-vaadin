package de.davherrmann.efficiently;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;
import java.util.function.Consumer;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;

import de.davherrmann.efficiently.components.Dialog.DialogProperties;
import de.davherrmann.efficiently.server.Action;
import de.davherrmann.efficiently.server.Dispatcher;

public class Dialog implements Element, BindingHolder<DialogProperties>
{
    private final Component dummy = new CssLayout();
    private final Map<String, Consumer<Object>> propertyBindings = newHashMap();
    private final Dispatcher dispatcher;

    public Dialog(final Dispatcher dispatcher)
    {
        this.dispatcher = dispatcher;

        final HorizontalLayout actionsLayout = new HorizontalLayout();
        final Window dialog = new PreventCloseWindow("Test", actionsLayout);
        dialog.center();

        bind(path()::hidden).to(hidden -> {
            if (!hidden)
            {
                dummy.getUI().addWindow(dialog);
            }
            else
            {
                dummy.getUI().removeWindow(dialog);
            }
        });
        bind(path()::actions).to(actions -> actions.forEach(action -> {
            final Button button = new Button(action.actionName());
            button.addClickListener(e -> dispatcher.dispatch(new Action(action.type())));
            actionsLayout.addComponent(button);
        }));
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

    private class PreventCloseWindow extends Window
    {
        public PreventCloseWindow(final String title, final Component content)
        {
            super(title, content);
        }

        @Override
        public void close()
        {
            dispatcher.dispatch(new Action("dialogAction/close"));
        }
    }
}
