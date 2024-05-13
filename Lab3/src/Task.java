import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.XMLConstants;
import org.w3c.dom.*;

import java.io.File;
import java.util.*;

public class Task {

    private static final String INPUT_XML = "Popular_Baby_Names_NY.xml";
    private static final String OUTPUT_XML = "output.xml";
    private static final String XSD_PATH = "Popular_Baby_Names_NY.xsd";

    public static void main(String[] args) {
        List<BabyName> babyNames = parseXML(INPUT_XML);
        validateXML(INPUT_XML, XSD_PATH);
        writeXML(babyNames, OUTPUT_XML);
        readAndDisplayXML(OUTPUT_XML);
    }

    private static List<BabyName> parseXML(String filePath) {
        List<BabyName> babyNames = new ArrayList<>();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                BabyName babyName = null;
                String content = null;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) {
                    if (qName.equalsIgnoreCase("row")) {
                        babyName = new BabyName();
                    }
                    content = ""; // Reset the content buffer at the start of any element
                }

                @Override
                public void characters(char[] ch, int start, int length) {
                    content += new String(ch, start, length).trim(); // Accumulate the characters into content
                }

                @Override
                public void endElement(String uri, String localName, String qName) {
                    if (babyName != null) {
                        switch (qName.toLowerCase()) {
                            case "brth_yr":
                                babyName.setBirthYear(content);
                                break;
                            case "gndr":
                                babyName.setGender(content);
                                break;
                            case "ethcty":
                                babyName.setEthnicity(content);
                                break;
                            case "nm":
                                babyName.setName(content);
                                break;
                            case "cnt":
                                babyName.setCount(content);
                                break;
                            case "rnk":
                                babyName.setRank(content);
                                break;
                            case "row":
                                babyNames.add(babyName);
                                babyName = null; // Ensure the current BabyName object is reset after being added
                                break;
                        }
                    }
                    content = ""; // Clear the content after handling it
                }
            };

            saxParser.parse(new File(filePath), handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return babyNames;
    }

    private static void validateXML(String xmlFile, String xsdFile) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(xsdFile));
            factory.setSchema(schema);

            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(new File(xmlFile), new DefaultHandler());
            System.out.println("Validation successful");
        } catch (Exception e) {
            System.out.println("Validation error: " + e.getMessage());
        }
    }

    private static void writeXML(List<BabyName> babyNames, String outputFile) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("PopularNames");
            doc.appendChild(rootElement);

            for (BabyName bn : babyNames) {
                Element row = doc.createElement("row");
                row.setAttribute("brth_yr", bn.getBirthYear());
                row.setAttribute("gndr", bn.getGender());
                row.setAttribute("ethcty", bn.getEthnicity());
                row.setAttribute("nm", bn.getName());
                row.setAttribute("cnt", bn.getCount());
                row.setAttribute("rnk", bn.getRank());
                rootElement.appendChild(row);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(outputFile));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void readAndDisplayXML(String filePath) {
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("row");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    System.out.println("Name : " + eElement.getAttribute("nm"));
                    System.out.println("Gender : " + eElement.getAttribute("gndr"));
                    System.out.println("Ethnicity : " + eElement.getAttribute("ethcty"));
                    System.out.println("Count : " + eElement.getAttribute("cnt"));
                    System.out.println("Rating : " + eElement.getAttribute("rnk"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class BabyName {
        private String birthYear;
        private String gender;
        private String ethnicity;
        private String name;
        private String count;
        private String rank;

        // Getters and Setters for all attributes
        public String getBirthYear() {
            return birthYear;
        }

        public void setBirthYear(String birthYear) {
            this.birthYear = birthYear;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getEthnicity() {
            return ethnicity;
        }

        public void setEthnicity(String ethnicity) {
            this.ethnicity = ethnicity;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }
    }

}
