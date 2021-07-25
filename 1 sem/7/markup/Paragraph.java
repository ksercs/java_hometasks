package markup;

import java.util.List;
import java.lang.StringBuilder;

public class Paragraph implements TextElement {
    protected List<TextElement> contents;
    
    Paragraph(List<TextElement> temp) {
        contents = temp;
    }
    
    public void toMarkdown(StringBuilder s) {
        for (int i = 0; i < contents.size(); i++) {
            contents.get(i).toMarkdown(s);
        }
    }
    
    public void toHtml(StringBuilder s) {
        for (int i = 0; i < contents.size(); i++) {
            contents.get(i).toHtml(s);
        }   
    }
}