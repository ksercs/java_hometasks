package crawler;

import java.io.*;
import java.util.*;

public class SimpleWebCrawler implements WebCrawler {
    private Downloader downloader;
    private Map<String, Page> pages = new HashMap<String, Page>(); // ссылки на страницы
    private Map<String, Image> images = new HashMap<String, Image>(); // ссылки на картинки

    public SimpleWebCrawler(Downloader temp) throws IOException {
        downloader = temp;
    }

    public Page crawl(String url, int depth) {
        if (pages.containsKey(url)) {
            return pages.get(url);
        } else if (depth == 0) {
            Page p = new Page(url, "");
            pages.put(url, p);
            return p;
        }
        List<String> curPages = new ArrayList<String>(); // ссылки на всех детей
        List<String> curImages = new ArrayList<String>(); // ссылки на картинки с этой страницы
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(downloader.download(url), "utf-8"));
            try {
                char c;
                while (in.ready()) {
                    StringBuilder tempStr = new StringBuilder();
                    c = (char) in.read();
                    if (c == (char) -1) {
                        break;
                    } else if (Character.isWhitespace(c)) {
                        continue;
                    } else if (c == '<') { // нашли тег
                        while (true) {
                            c = (char) in.read(); 
                            if (Character.isWhitespace(c) && c != ' ') {
                                continue;
                            }
                            if (c == ' ' || c == '>') {
                                break;
                            }
                            tempStr.append(c);
                        }
                        String temp = tempStr.toString();
                        tempStr.delete(0, tempStr.length());

                        if (temp.equals("!--")) { // нашли коммент
                            while (in.ready()) {
                                while (c != '-') {
                                    c = (char) in.read();
                                }
                                c = (char) in.read();
                                if (c == '-') {
                                    c = (char) in.read();
                                    if (c == '>') {
                                        break;
                                    }
                                }
                            }
                        } else if (temp.equals("title")) { // нашли титл
                            while (in.ready()) {
                                c = (char) in.read();
                                if (c == '<') {
                                    break;
                                }
                                tempStr.append(c);
                            }
                            pages.put(url, new Page(url, noHtmlCodes(tempStr.toString()))); // создается страница в мепке
                        } else if (temp.equals("a")) { // обработка ребенка
                            temp = getParam(in, "href"); // получение ссылки
                            if (temp == null) {
                                continue;
                            }
                            temp = transform(url, temp); // из относительной в абсолютную
                            curPages.add(noHtmlCodes(temp)); // кладем ребенка в список детей с экранированием
                        } else if (temp.equals("img")) { // обработка картинки
                            temp = getParam(in, "src"); // ребенок картинки
                            temp = transform(url, temp);
                            String imageName = getImageName(temp);
                            if (!images.containsKey(temp)) { // запись картинки
                                images.put(temp, new Image(temp, imageName));
                                InputStream inImage = downloader.download(temp);
                                try {
                                    OutputStream outImage = new FileOutputStream(imageName);
                                    try { // запись картинки в файловюу систему
                                        byte[] buffer = new byte[1 << 16];
                                        int r;
                                        while ((r = inImage.read(buffer)) != -1) {
                                            for (int i = 0; i < r; i++) {
                                                outImage.write(buffer[i]);
                                            }
                                        }
                                    } finally {
                                        outImage.close();
                                    }
                                } catch (FileNotFoundException e) {
                                    System.out.println(e.getMessage());
                                } finally {
                                    inImage.close();
                                }
                            }
                            curImages.add(temp); // добавили ребенка
                        }
                    }
                }
            } finally {
                in.close();
            }
        } catch(IOException e) {
            System.out.println("IOException!");
            return new Page(url, "");
        }
        for (String tempPage : curPages) {
            pages.get(url).addLink(crawl(tempPage, depth - 1)); // добавление в страницу детей и картинок
        }
        for (String tempImage : curImages) {
            pages.get(url).addImage(images.get(tempImage));
        }
        return pages.get(url);
    }

    private String getParam(Reader in, String find) throws IOException { // получение значение параметра внутри тега
        StringBuilder tempStr = new StringBuilder();
        String ans = null;
        char c;
        while(ans == null) {
            while (in.ready()) {
                c = (char) in.read();
                if (Character.isWhitespace(c)) {
                    continue;
                }
                if (c == '=') {
                   break;
                } 
                if (c == '>') {
                    return null;
                }
                tempStr.append(c);
            }
            String param = tempStr.toString();
            tempStr.delete(0, tempStr.length());
            c = (char) in.read();
            while(Character.isWhitespace(c)) {
                c = (char) in.read();
            }
            while (in.ready()) {
                c = (char) in.read();
                if (Character.isWhitespace(c)) {
                    continue;
                }
                if (c == '"') {
                    break;
                }
                tempStr.append(c);
            }
            String value = tempStr.toString();
            tempStr.delete(0, tempStr.length());
            if (param.equals(find)) {
                ans = value;
            }
        }
        return ans;
    }

    private boolean checkRegex(String str, String reg) { 
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(reg);
        java.util.regex.Matcher m = p.matcher(str);
        return m.matches();
    }

    private String transform(String url, String temp) { 
        if (temp.equals("..")) {
            temp += "/";
        }
        if (checkRegex(temp, ".*#.*") && !checkRegex(temp, ".*html#.*")) { //ссылка на саму себя и на брата самого себя
            return url;
        } else if (checkRegex(temp, "https?://.*")) { // трушная ссылка
            return temp;
        }
        if (temp.charAt(0) == '/') {
            if (temp.length() > 1 && temp.charAt(1) == '/') {
                return "http://" + temp.substring(2, temp.length());
            }
            int i;
            int depth = 0;
            for (i = 0; i < url.length() && depth != 3; i++) {
                if (url.charAt(i) == '/') {
                    depth++;
                }
            }
            String root = url.substring(0, i);
            if (temp.length() == 1) {
                return root;
            } else {
                return root + temp.substring(1, temp.length());
            }
        }
        int depth = 0;
        int i = 0;
        while(i < temp.length() && temp.charAt(i) == '.') { 
            i += 3;
            depth++;
        }
        depth++;
        int j;
        for (j = url.length() - 1; j >= 0 && depth > 0; j--) {
            if (url.charAt(j) == '/') {
                depth--;
            }
        }
        int k = 0;
        for (k = i; k < temp.length(); k++) {
            if (temp.charAt(k) == '#') {
                break;
            }
        }
        return url.substring(0, j + 2) + temp.substring(i, k);
    }

    private String noHtmlCodes(String temp) {
        return temp.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&");
    }

    private String getImageName(String name) { 
        int save = 0;
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == '/') {
                save = i;
            }
        }
        int save2;
        for (save2 = save; save2 < name.length(); save2++) {
            if (name.charAt(save2) == '?') {
                break;
            }
        }
        return name.substring(save + 1, save2);
    }
}
