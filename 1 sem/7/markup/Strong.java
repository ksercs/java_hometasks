package markup;

import java.util.List;
import java.lang.StringBuilder;

public class Strong extends Paragraph {
    Strong(List<TextElement> temp) {
        super(temp);
    }
    
    public void toMarkdown(StringBuilder s) {
        s.append("__");
        super.toMarkdown(s);
        s.append("__");
    }
    
    public void toHtml(StringBuilder s) {
        s.append("<strong>");
        super.toHtml(s);
        s.append("</strong>");
    }
}