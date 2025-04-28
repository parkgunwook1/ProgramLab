package kr.co.parkcom.store.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ConfigMapReader {
    private final Map<String , String> configMap = new HashMap<>();

    public void loadConfig(String fileName) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) throw new IllegalArgumentException("파일을 찾을 수 없습니다." + fileName);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);
            doc.getDocumentElement().normalize();

            NodeList entries = doc.getElementsByTagName("entry");
            for (int i = 0; i < entries.getLength(); i++) {
                Element entry = (Element) entries.item(i);
                String key = entry.getAttribute("key");
                String value = entry.getTextContent();
                configMap.put(key , value);
            }
        } catch (Exception e) {
            System.out.println("file error");
        }
    }

    public String get(String key) {
        return configMap.get(key);
    }
}
