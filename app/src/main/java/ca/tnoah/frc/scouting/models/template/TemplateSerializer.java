package ca.tnoah.frc.scouting.models.template;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class TemplateSerializer {

    public static TemplateData deserialize(String xml) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            Document document = builder.parse(is);
            document.getDocumentElement().normalize();

            Element rootElement = document.getDocumentElement();

            TemplateData templateData = new TemplateData(
                    rootElement.getAttribute("name"),
                    rootElement.getAttribute("type"),
                    rootElement.getAttribute("year")
            );
            List<TemplateData.Section> sections = new ArrayList<>();

            NodeList nodeList = rootElement.getElementsByTagName("Section");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element sElement = (Element) node;

                    sections.add(deserializeSection(sElement));
                }
            }

            templateData.sections = sections;

            Log.d("==TemplateSerializer==", "Made it here");
            return templateData;

        } catch (ParserConfigurationException | IOException | SAXException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private static TemplateData.Section deserializeSection(Element eSection) {
        TemplateData.Section section = new TemplateData.Section();
        section.header = eSection.getElementsByTagName("Header").item(0).getTextContent();
        List<TemplateData.Field> fields = new ArrayList<>();

        NodeList nodeList = eSection.getElementsByTagName("Fields").item(0).getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element fElement = (Element) node;

                switch (fElement.getTagName()) {
                    case "TextField":
                        fields.add(deserializeTextField(fElement));
                        break;
                    case "RadioList":
                        fields.add(deserializeRadioList(fElement));
                        break;
                    case "CheckList":
                        fields.add(deserializeCheckList(fElement));
                        break;
                    case "Counter":
                        fields.add(deserializeCounter(fElement));
                        break;
                    default:
                        break;
                }
            }
        }

        section.fields = fields;
        return section;
    }

    private static TemplateData.TextField deserializeTextField(Element e) {
        TemplateData.TextField textField = new TemplateData.TextField();
        addGeneric(textField, e);

        textField.value = e.getAttribute("value");
        textField.multiline = e.getAttribute("multiline").equals("true");

        return textField;
    }

    private static TemplateData.RadioList deserializeRadioList(Element e) {
        TemplateData.RadioList radioList = new TemplateData.RadioList();
        addGeneric(radioList, e);

        radioList.other = e.getAttribute("other").equals("true");

        NodeList nodeList = e.getElementsByTagName("Item");
        radioList.items = deserializeItems(nodeList);

        return radioList;
    }

    private static TemplateData.CheckList deserializeCheckList(Element e) {
        TemplateData.CheckList checkList = new TemplateData.CheckList();
        addGeneric(checkList, e);

        NodeList nodeList = e.getElementsByTagName("Item");
        checkList.items = deserializeItems(nodeList);

        return checkList;
    }

    private static TemplateData.Counter deserializeCounter(Element e) {
        TemplateData.Counter counter = new TemplateData.Counter();
        addGeneric(counter, e);

        counter.value = convertStringToInt(e.getAttribute("value"));
        counter.max = convertStringToInt(e.getAttribute("max"));
        counter.min = convertStringToInt(e.getAttribute("min"));

        return counter;
    }

    private static List<TemplateData.Item> deserializeItems(NodeList nodeList) {
        List<TemplateData.Item> items = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element iElement = (Element) node;

                items.add(deserializeItem(iElement));
            }
        }

        return items;
    }

    private static TemplateData.Item deserializeItem(Element e) {
        TemplateData.Item item = new TemplateData.Item();
        item.label = e.getAttribute("label");
        item.selected = e.getAttribute("selected").equals("true");

        return item;
    }

    private static <T extends TemplateData.Field> void addGeneric(T field, Element e) {
        field.label = e.getAttribute("label");
        field.required = e.getAttribute("required").equals("true");
    }

    private static int convertStringToInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

}