package markup;

import java.lang.StringBuilder;

public class Text implements TextElement {
    private String contents;
    
    Text(String s) {
        contents = s;
    }   
    
    public void toMarkdown(StringBuilder s) {
        s.append(contents);
    }
    
    public void toHtml(StringBuilder s) {
        s.append(contents);
    }
}