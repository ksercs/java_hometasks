package md2html;

import java.util.*;
import java.io.*;

public class Md2Html {
    private static Map<String, String> htmlOpen = new HashMap<>();
    private static Map<String, String> htmlClose = new HashMap<>();
    private static Map<Character, String> screenHtml = new HashMap<>();
    private static Set<Character> singleSmb = new HashSet<>();
    
    private static void initializeStructs() {
        htmlOpen.put("__", "<strong>");
        htmlClose.put("__", "</strong>");
        htmlOpen.put("**", "<strong>");
        htmlClose.put("**", "</strong>");
        htmlOpen.put("`", "<code>");
        htmlClose.put("`", "</code>");
        htmlOpen.put("*", "<em>");
        htmlClose.put("*", "</em>");
        htmlOpen.put("_", "<em>");
        htmlClose.put("_", "</em>");
        htmlOpen.put("--", "<s>");
        htmlClose.put("--", "</s>");
        htmlOpen.put("++", "<u>");
        htmlClose.put("++", "</u>");
        screenHtml.put('<', "&lt;");
        screenHtml.put('>', "&gt;");
        screenHtml.put('&', "&amp;");
        singleSmb.add('-');
        singleSmb.add('*');
        singleSmb.add('_');
        singleSmb.add('+');
    }
    
    private static void getAnswer(String curContr, Map<String, Integer> contr, StringBuilder str) {
        List<String> tempList = new ArrayList<String>(contr.keySet());
        if (!contr.containsKey(curContr)) {
            contr.put(curContr, str.length());
        } else {
            for (int i = tempList.size() - 1; i >= 0; --i) {
                String tmp = tempList.get(i);
                if (tmp.equals(curContr)) {
                    str.insert(contr.get(tmp), htmlOpen.get(tmp));
                    str.append(htmlClose.get(tmp));
                    contr.remove(tmp);
                    break;
                } else {
                    str.insert(contr.get(tmp), tmp);
                    contr.remove(tmp);
                }
            }
        }
    }
    
    private static void handleParagraph(Reader in, Writer out, char c, Map<String, Integer> contr, StringBuilder str) throws IOException {
        StringBuilder tempStr = new StringBuilder();
        while (c == '\r' || c == '\n') {
            c = (char) in.read();
        }
        while (c == '#') {
            tempStr.append(c);
            c = (char) in.read();
        }
        if (tempStr.length() == 0 || c != ' ') {
            out.write("<p>" + tempStr.toString());
            checkSmb(in, out, 1, c, contr, str);
            out.write("</p>\n");
        } else {
            out.write("<h" + tempStr.length() + ">");
            checkSmb(in, out, 1, (char) in.read(), contr, str);
            out.write("</h" + tempStr.length() + ">\n");
        }
        contr.clear();
    }
    
    private static boolean handleText(Reader in, Writer out, char c, Map<String, Integer> contr, StringBuilder str) throws IOException {
        if (singleSmb.contains(c)) {
            String curContr = "" + c;
            boolean needToNext = false;
            char save = c;
            c = (char) in.read();
            if (c == save) {
                curContr += c;
                needToNext = true;
            } else if (save == '-') {
                str.append(save);
            }
            if (!curContr.equals("-") && !curContr.equals("+")) {
                getAnswer(curContr, contr, str);
            } 
            if (!needToNext) {
                return true;
            }
        } else if (c == '`') {
            getAnswer(c + "", contr, str);
        } else if (c == '\\') {
            str.append((char) in.read());
        } else if (screenHtml.containsKey(c)) {
            str.append(screenHtml.get(c));
        } else {
            str.append(c);
        }
        return false;
    }
    
    private static void checkSmb(Reader in, Writer out, int level, char last, Map<String, Integer> contr, StringBuilder str) throws IOException {
        boolean fl = false; // были ли возврат каретки + перевод строки на предыдущей итерации
        char c = last;
        while (true) {
            if ((c == (char) -1) || (fl && c == '\r')) {
                if (c == '\r') {
                    in.read();
                }
                List<String> tempList = new ArrayList<String>(contr.keySet());
                for (int i = tempList.size() - 1; i >= 0; --i) {
                    String tmp = tempList.get(i);
                    str.insert(contr.get(tmp), tmp);
                    contr.remove(tmp);
                }
                out.write(str.toString());
                str.delete(0, str.length());
                return;
            }
            if (level == 0) {
                handleParagraph(in, out, c, contr, str);
            } else {
                if (fl) {
                    fl = false;
                    str.append('\n');
                }
                if (c == '\r') {
                    in.read();
                    fl = true;
                } else {
                    if (handleText(in, out, c, contr, str)) {
                        continue;
                    }
                }
            }
            c = (char) in.read();
        }
    }

    public static void main(String[] argc) {
        if (argc.length != 2) {
            System.out.println("Need 2 files!");
            return;
        }
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(argc[0]), "utf-8"));
            try {
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(argc[1]), "utf-8"));
                try {
                    initializeStructs();
                    checkSmb(in, out, 0, (char) in.read(), new LinkedHashMap<String, Integer>(), new StringBuilder());
                } finally {
                    out.close();
                }
            } catch (FileNotFoundException e) {
                System.out.println("Cannot open output file");
            } catch (UnsupportedEncodingException e) {
                System.out.println("Unsupported encoding(utf-8)");
            } catch (IOException e) {
                System.out.println("IOException!");
            } finally {
                in.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find input file");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Unsupported encoding(UTF-8)");
        } catch (IOException e) {
            System.out.println("IOException!");
        }
    }
}
