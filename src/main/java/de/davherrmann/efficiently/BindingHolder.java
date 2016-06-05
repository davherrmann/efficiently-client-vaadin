package de.davherrmann.efficiently;

import static com.google.common.base.Joiner.on;
import static de.davherrmann.immutable.PathRecorder.pathInstanceFor;
import static java.lang.String.format;
import static java.util.Arrays.stream;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import de.davherrmann.immutable.PathRecorder;

public interface BindingHolder<I> extends Bindable
{
    default void changeProperty(String property, Object value)
    {
        propertyBindings() //
            .getOrDefault(property, e -> warnNoPropertyBindingFound(property)) //
            .accept(value);
    }

    default void warnNoPropertyBindingFound(final String property)
    {
        System.out.println(format("No property binding in %s for %s", this.getClass(), property));
    }

    Map<String, Consumer<Object>> propertyBindings();

    default <T> BindTo<I, T> bind(Supplier<T> property)
    {
        return new BindTo<>(this, property);
    }

    default I path()
    {
        return pathInstanceFor(propertyType());
    }

    @SuppressWarnings("unchecked")
    default Class<I> propertyType()
    {
        return (Class<I>) stream(this.getClass().getGenericInterfaces()) //
            .filter(i -> i instanceof ParameterizedType) //
            .map(i -> (ParameterizedType) i) //
            .filter(i -> BindingHolder.class.equals(i.getRawType())) //
            .findFirst() //
            .map(ParameterizedType::getActualTypeArguments) //
            .get()[0];
    }

    class BindTo<I, T>
    {
        private final BindingHolder<I> bindingHolder;
        private final Supplier<T> property;

        public BindTo(final BindingHolder<I> BindingHolder, final Supplier<T> property)
        {
            this.bindingHolder = BindingHolder;
            this.property = property;
        }

        public void to(Consumer<T> consumer)
        {
            final PathRecorder<?> pathRecorder = PathRecorder.pathRecorderInstanceFor(bindingHolder.propertyType());
            final List<String> path = pathRecorder.pathFor(property);
            bindingHolder.propertyBindings().put(on(".").join(path), (Consumer<Object>) consumer);
        }
    }
}
