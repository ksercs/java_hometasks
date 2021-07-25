package crawler;

import java.io.*;
import java.util.*;
import java.net.URL;

public class SimpleWebCrawler implements WebCrawler {
    private  Downloader downloader;
    private  Map<String, Page> pages = new HashMap<String, Page>();
    private  Map<String, Image> images = new HashMap<String, Image>();

    public SimpleWebCrawler(Downloader temp) throws IOException {
        downloader = temp;
    }

    public Page crawl(String url, int depth) {
        if (pages.containsKey(url)) {
            return pages.get(url);
        } 
        if (depth == 0) {
            Page p = new Page(url, "");
            pages.put(url, p);
            return p;
        }
        List<String> curPages = new ArrayList<String>();
        List<String> curImages = new ArrayList<String>();
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
                    } else if (c == '<') {
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

                        if (temp.equals("!--")) {
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
                        } else if (temp.equals("title")) {
                            while (in.ready()) {
                                c = (char) in.read();
                                if (c == '<') {
                                    StringBuilder com = new StringBuilder();
                                    boolean flag = false;
                                    while(in.ready()) {
                                        c = (char) in.read();
                                        if (c == '>') {
                                            if (com.toString().equals("/title")) {
                                                flag = true;
                                            }
                                            break;                                        
                                        }
                                        com.append(c);
                                    }
                                    if (flag == true) {
                                        break;
                                    }
                                    tempStr.append('<');
                                    tempStr.append(com.toString());
                                    tempStr.append('>');
                                }
                                else {
                                    tempStr.append(c);
                                }
                            }
                            pages.put(url, new Page(url, noHtmlCodes(tempStr.toString())));
                        } else if (temp.equals("a")) {
                            temp = getAtr(in, "href");
                            if (temp == null) {
                                continue;
                            }
                            temp = new URL(new URL(url), temp).toString();
                            if (temp.indexOf("#") != -1) {
                                temp = temp.substring(0, temp.indexOf("#"));
                            }
                            curPages.add(noHtmlCodes(temp));
                        } else if (temp.equals("img")) {
                            temp = getAtr(in, "src");
                            temp = new URL(new URL(url), temp).toString();
                            if (!images.containsKey(temp)) {
                                String imageName = getImageName(temp);
                                images.put(temp, new Image(temp, imageName));
                                InputStream inImage = downloader.download(temp);
                                try {
                                    OutputStream outImage = new FileOutputStream(imageName);
                                    try {
                                        byte[] buf = new byte[1 << 16];
                                        int r;
                                        while ((r = inImage.read(buf)) != -1) {
                                            for (int i = 0; i < r; i++) {
                                                outImage.write(buf[i]);
                                            }
                                        }
                                    } finally {
                                        outImage.close();
                                    }
                                } catch (FileNotFoundException e) {
                                    System.out.println("FileNotFoundException!\n");
                                } finally {
                                    inImage.close();
                                }
                                urImages.add(temp);
                            }
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
            pages.get(url).addLink(crawl(tempPage, depth - 1));
        }
        for (String tempImage : curImages) {
            pages.get(url).addImage(images.get(tempImage));
        }
        return pages.get(url);
    }

    private static String getAtr(Reader in, String find) throws IOException {
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

    private static String noHtmlCodes(String temp) {
        return temp.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&mdash;", "\u2014").replaceAll("&nbsp;", "\u00A0");
    }

    private static String getImageName(String name) { 
        int ind = 0;
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == '/') {
                ind = i;
            }
        }
        return name.substring(ind + 1);
    }
}