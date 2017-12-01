package com.crocoro.file.type;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * XML读写工具类
 */
@SuppressWarnings("unchecked")
public class XMLHelper {
    private Document doc;
    private File xmlFile;

    public XMLHelper() {
    }

    public XMLHelper(String xmlFile) throws DocumentException {
        this(new File(xmlFile));
    }

    public XMLHelper(File xmlFile) throws DocumentException {
        this.xmlFile = xmlFile;
        this.doc = readXML(xmlFile);
    }

    public XMLHelper(Document doc) {
        this.doc = doc;
    }

    /**
     * 读取XML文件
     *
     * @param xmlFile XML文件路径
     * @return DOM
     * @throws DocumentException
     */
    public static Document readXML(String xmlFile) throws DocumentException {
        return readXML(new File(xmlFile));
    }

    /**
     * 读取XML文件
     *
     * @param xmlFile XML文件对象
     * @return DOM
     * @throws DocumentException
     */
    public static Document readXML(File xmlFile) throws DocumentException {
        SAXReader reader = new SAXReader();
        return reader.read(xmlFile);
    }

    /**
     * 根据属性来取值
     *
     * @param doc  文档
     * @param ele  元素
     * @param name 名字
     * @param attr 属性名
     * @param val  属性值
     * @return 节点值
     */
    public String getElementTextByAttr(Document doc, String ele, String name, String attr, String val) {
        List<Element> list = doc.getRootElement().element(ele).elements(name);
        for (Element el : list) {
            if (el.attribute(attr).getValue().equals(val)) {
                return el.getText();
            }
        }
        return null;
    }

    /**
     * 根据路径取值
     *
     * @param path 路径
     * @return 节点值
     */
    public String getElementText(String path) {
        String[] pathEle = path.split("/");
        boolean isResult = false;
        Element ele = doc.getRootElement();
        for (String patz : pathEle) {
            ele = ele.element(patz);
            isResult = true;
        }
        if (isResult)
            return ele.getText();
        return null;
    }

    /**
     * 根据路径取元素
     *
     * @param path 路径
     * @return 元素列表
     */
    public Element getElement(String path) {
        String[] pathEle = path.split("/");

        Element ele = doc.getRootElement();
        for (int i = 0; i < pathEle.length; i++) {
            ele = ele.element(pathEle[i]);
        }
        return ele;
    }

    /**
     * 根据路径取元素列表
     *
     * @param path 路径
     * @return 元素列表
     */
    public List<Element> getElements(String path) {
        String[] pathEle = path.split("/");

        Element ele = doc.getRootElement();
        for (int i = 0; i < pathEle.length - 1; i++) {
            ele = ele.element(pathEle[i]);
        }
        List<Element> eles = ele.elements(pathEle[pathEle.length - 1]);
        return eles;
    }

    /**
     * 根据路径取值列表
     *
     * @param path 路径
     * @return 值列表
     */
    public List<String> getElementsText(String path) {
        List<String> result = new ArrayList<>();
        List<Element> eles = getElements(path);

        for (Element ele : eles) {
            result.add(ele.getText());
        }

        if (result.size() > 0)
            return result;
        return null;
    }

    /**
     * 根据属性获取元素属性
     *
     * @param path         路径
     * @param attrName     属性名
     * @param defaultValue 默认值
     * @return 属性
     */
    public String getElementAttr(String path, String attrName, String defaultValue) {
        return getElement(path).attributeValue(attrName, defaultValue);
    }

    /**
     * 根据属性获取元素属性值
     *
     * @param path         路径
     * @param attrName     属性名
     * @param defaultValue 默认值
     * @return 值列表
     */
    public List<String> getElementsAttr(String path, String attrName, String defaultValue) {
        List<String> result = new ArrayList<>();
        List<Element> eles = getElements(path);

        for (Element ele : eles) {
            result.add(ele.attributeValue(attrName, defaultValue));
        }
        if (result.size() > 0)
            return result;
        return null;
    }

    public String getElementAttr(String path, int index, String attrName, String defaultValue) {
        List<Element> eles = getElements(path);
        Element ele = eles.get(index);
        return ele.attributeValue(attrName, defaultValue);
    }

    /**
     * 根据元素属性与路径取值
     *
     * @param path     路径
     * @param attrName 属性名
     * @return 值列表
     */
    public List<String> getElementsTextByAttr(String path, String attrName, String attrValue) {
        List<String> result = new ArrayList<>();
        List<Element> eles = getElementsByAttr(path, attrName, attrValue);
        for (Element ele : eles) {
            result.add(ele.getText());
        }
        return result;
    }

    /**
     * 根据元素属性与路径获取属性值
     *
     * @param path           路径
     * @param attrName       属性名
     * @param attrValue      属性值
     * @param targetAttrName 目标属性名
     * @return 值列表
     */
    public List<String> getElementsAttrByAttr(String path, String attrName, String attrValue, String targetAttrName) {
        List<String> result = new ArrayList<>();
        List<Element> eles = getElementsByAttr(path, attrName, attrValue);
        for (Element ele : eles) {
            result.add(ele.attributeValue(targetAttrName, ""));
        }
        return result;
    }

    /**
     * 根据元素属性与路径取元素列表
     *
     * @param path     路径
     * @param attrName 属性名
     * @return 元素列表
     */
    public List<Element> getElementsByAttr(String path, String attrName, String attrValue) {
        List<Element> result = new ArrayList<>();
        List<Element> eles = getElements(path);
        for (Element ele : eles) {
            if (ele.attributeValue(attrName, null).equals(attrValue)) {
                result.add(ele);
            }
        }
        return result;
    }

    public void setElementTextByAttr(Document doc, String ele, String name, String attr, String val, String target) {
        List<Element> list = doc.getRootElement().element(ele).elements(name);
        for (Element el : list) {
            if (el.attribute(attr).getValue().equals(val)) {
                el.setText(target);
            }
        }
    }

    public void setElementText(String path, String text) throws IOException, DocumentException {
        Element ele = getElement(path);
        ele.setText(text);
        writeXML(xmlFile);
    }

    public void setElementTextByAttr(String path, String text, String attrName, String attrValue) throws IOException, DocumentException {
        List<Element> eles = getElements(path);
        for (Element ele : eles) {
            if (ele.attributeValue(attrName, null).equals(attrValue)) {
                ele.setText(text);
                break;
            }
        }
        writeXML(xmlFile);
    }

    public void setElementAttr(String path, String attrName, String attrValue) throws IOException, DocumentException {
        Element ele = getElement(path);
        ele.addAttribute(attrName, attrValue);
        writeXML(xmlFile);
    }

    public void setElementAttrByAttr(String path, String attrName, String attrValue, String targetAttrValue) throws IOException, DocumentException {
        List<Element> eles = getElements(path);
        for (Element ele : eles) {
            if (attrValue.equals(ele.attributeValue(attrName, null))) {
                ele.addAttribute(attrName, targetAttrValue);
                break;
            }
        }
        writeXML(xmlFile);
    }


    /**
     * 写入XML文件
     *
     * @param xmlFile 文件路径
     * @throws IOException
     */
    public void writeXML(String xmlFile) throws IOException, DocumentException {
        if (doc != null) {
            writeXML(doc, xmlFile);
        }
    }

    /**
     * 写入XML文件
     *
     * @param xmlFile 文件对象
     * @throws IOException
     */
    public void writeXML(File xmlFile) throws IOException, DocumentException {
        if (doc != null) {
            writeXML(doc, xmlFile);
        }
    }

    /**
     * 写入XML文件
     *
     * @param doc     DOM对象
     * @param xmlFile 文件路径
     * @throws IOException
     */
    public void writeXML(Document doc, String xmlFile) throws IOException, DocumentException {
        writeXML(doc, new File(xmlFile));
    }

    /**
     * 写入XML文件
     *
     * @param doc     DOM对象
     * @param xmlFile 文件对象
     * @throws IOException
     */
    public void writeXML(Document doc, File xmlFile) throws IOException, DocumentException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new FileWriter(xmlFile), format);
        writer.write(doc);
        writer.close();
        this.doc = readXML(xmlFile);
    }
}
