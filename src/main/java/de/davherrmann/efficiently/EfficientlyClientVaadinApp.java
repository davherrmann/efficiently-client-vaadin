package de.davherrmann.efficiently;

import javax.inject.Inject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import de.davherrmann.efficiently.app.MySpecialState;
import de.davherrmann.efficiently.app.MySpecialView;
import de.davherrmann.efficiently.components.View;
import de.davherrmann.efficiently.server.Action;
import de.davherrmann.efficiently.server.ClientData;
import de.davherrmann.efficiently.server.Dispatcher;
import de.davherrmann.efficiently.server.Efficiently;
import de.davherrmann.immutable.Immutable;

@SpringBootApplication
public class EfficientlyClientVaadinApp
{

    public static void main(String[] args)
    {
        SpringApplication.run(EfficientlyClientVaadinApp.class, args);
    }

    @Theme("valo")
    @SpringUI(path = "/")
    public static class VaadinUI extends UI implements Dispatcher
    {
        private final ViewRenderer viewRenderer = new ViewRenderer(this);
        private final View view = new MySpecialView();
        private final Efficiently efficiently;

        @Inject
        public VaadinUI(final Efficiently efficiently)
        {
            this.efficiently = efficiently;
        }

        @Override
        protected void init(VaadinRequest request)
        {
            final Button button = new Button("test");
            final Panel content = new Panel();

            button.addClickListener(event -> {
                efficiently.reset();
                // TODO better (stateless) way to delete bindings?
                viewRenderer.init();
                final Element element = viewRenderer.createComponentFrom(view.create().template());
                content.setContent(element.component());
                dispatch(new Action("initState"));
            });

            setContent(new VerticalLayout(button, content));
        }

        @Override
        public void dispatch(final Action action)
        {
            final ClientData clientData = new ClientData(new Immutable<>(MySpecialState.class), action);
            final Immutable<MySpecialState> diff = efficiently.reduce(clientData);
            viewRenderer.update(diff);
        }
    }
}
