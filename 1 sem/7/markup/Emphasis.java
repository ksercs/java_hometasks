package markup;

import java.util.List;
import java.lang.StringBuilder;

public class Emphasis extends Paragraph {
    Emphasis(List<TextElement> temp) {
        super(temp);
    }
    
    public void toMarkdown(StringBuilder s) {
        s.append("*");
        super.toMarkdown(s);
        s.append("*");
    }
    
    public void toHtml(StringBuilder s) {
        s.append("<em>");
        super.toHtml(s);
        s.append("</em>");
    }
}