package de.davherrmann.efficiently;

public class Binding
{
    private final String source;
    private final Element element;
    private final String property;

    public Binding(final String source, final Element element, final String property)
    {

        this.source = source;
        this.element = element;
        this.property = property;
    }

    public String source()
    {
        return source;
    }

    public Element element()
    {
        return element;
    }

    public String property()
    {
        return property;
    }
}
