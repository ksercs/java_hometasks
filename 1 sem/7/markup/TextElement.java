package markup;

public interface TextElement {
    void toMarkdown(StringBuilder s);
    void toHtml(StringBuilder s);
}