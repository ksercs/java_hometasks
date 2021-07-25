package markup;

import java.util.List;
import java.lang.StringBuilder;

public class Strikeout extends Paragraph {
    Strikeout(List<TextElement> temp) {
        super(temp);
    }
    
    public void toMarkdown(StringBuilder s) {
        s.append("~");
        super.toMarkdown(s);
        s.append("~");
    }
    
    public void toHtml(StringBuilder s) {
        s.append("<s>");
        super.toHtml(s);
        s.append("</s>");
    }
}