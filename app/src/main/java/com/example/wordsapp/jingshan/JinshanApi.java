package com.example.wordsapp.jingshan;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;

public class JinshanApi {

    public  String translation (String word) throws Exception {
        URL u = new URL("http://dict-co.iciba.com/api/dictionary.php?w=" + word + "&key=9AA9FA4923AC16CED1583C26CF284C3F");
        InputStream in=u.openStream();
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        try {
            byte buf[]=new byte[1024];
            int read = 0;
            while ((read = in.read(buf)) > 0) {
                out.write(buf, 0, read);
            }
        }  finally {
            if (in != null) {
                in.close();
            }
        }
        byte b[]=out.toByteArray( );
        String str = new String(b,"utf-8");
        int i = str.lastIndexOf("rate");
        int j = str.lastIndexOf("update");

        System.out.print(str);

       return   JinshanXMLWithPull(str);
    }


    /**
     *
     * 使用Pull方式解析金山词霸返回的XML数据。
     *这里只解析了例句
     *
     * */
    public static String JinshanXMLWithPull(String result) {

        String exampleText = "";    //例句信息
        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(result));
            int eventType = xmlPullParser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    //开始解析
                    case XmlPullParser.START_TAG: {
                        switch (nodeName) {
                            case "orig":
                                exampleText += xmlPullParser.nextText();
                                exampleText = exampleText.substring(0,exampleText.length()-1);
                                break;
                            case "trans":
                                exampleText += xmlPullParser.nextText();
                                break;
                            default:
                                break;
                        }
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TAG", "解析过程中出错！！！");
        }

        exampleText = exampleText.substring(1,exampleText.length());
        return exampleText;

    }


}
