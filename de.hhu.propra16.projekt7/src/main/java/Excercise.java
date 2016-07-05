import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Excercise {

    private File file;
    private Document doc;

    private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder builder = factory.newDocumentBuilder();

    private String name;
    private Element root;
    private Node classes;
    private Node tests;

    /*
     * Dieser Construktor erstellt eine leere XML Datei mit dem Aufbau:
     *
     * <?xml version="1.0" encoding="UTF-8" standalone="no"?>
     * <excercise>
     *     <classes/>
     *     <tests/>
     * </excercise>
     *
     * Die Funktionen addClassNode und addTestNode fügen eine class oder einen test hinzu
     */
    public Excercise(String name) throws ParserConfigurationException, TransformerException {
        this.name = name;
        this.doc = builder.newDocument();
        this.root = doc.createElement("excercise");
        this.root.setAttribute("name",name);
        doc.appendChild(root);

        if(classes == null)
            this.classes = doc.createElement("classes");

        if(tests == null)
            this.tests = doc.createElement("tests");

        root.appendChild(classes);
        root.appendChild(tests);

        saveEx();
    }

    /*
     * Konstruktor liest eine XML Datei ein
     */
    public Excercise(File file) throws ParserConfigurationException, IOException, SAXException {
        this.file = file;
        loadEx();
    }

    public void loadEx() throws ParserConfigurationException, IOException, SAXException {
        this.doc = builder.parse(file);
        this.root = doc.getDocumentElement();
        this.name = root.getAttribute("name");

        NodeList tmp = root.getElementsByTagName("classes");
        this.classes = tmp.item(0);

        tmp = root.getElementsByTagName("tests");
        this.tests = tmp.item(0);
    }

    public void saveEx() throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        DOMSource src = new DOMSource(doc);

        this.file = new File(name+".xml");

        StreamResult fileResult = new StreamResult(file);
        transformer.transform(src, fileResult);
    }

    /*
     * Fügt eine class mit dem übergebenen Namen und Text hinzu
     */
    public void addClassNode(String name, String text) throws ParserConfigurationException, TransformerException {
        Element classNode = doc.createElement("class");
        classNode.setAttribute("name", name);
        classNode.setTextContent(text);

        this.classes.appendChild(classNode);
        saveEx();
    }

    /*
     * Fügt einen test mit dem übergebenen Namen und Text hinzu
     */
    public void addTestNode(String name, String text) throws ParserConfigurationException, TransformerException {
        Element testNode = doc.createElement("test");
        testNode.setAttribute("name", name);
        testNode.setTextContent(text);

        this.tests.appendChild(testNode);
        saveEx();
    }

    /*
     * Gibt eine ArrayListe mit Strings der classes zurück
     */
    public ArrayList<String> getClassText(){
        NodeList classList = classes.getChildNodes();
        ArrayList<String> classText = new ArrayList<>();

        for(int i = 0; i < classList.getLength(); i++){
            Node node = classList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                classText.add(element.getTextContent());
            }
        }

        return classText;
    }

    /*
     * Gibt eine ArrayListe mit Strings der tests zurück
     */
    public ArrayList<String> getTestText(){
        NodeList testList = tests.getChildNodes();
        ArrayList<String> testText = new ArrayList<>();

        for(int i = 0; i < testList.getLength(); i++){
            Node node = testList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                testText.add(element.getTextContent());
            }
        }

        return testText;
    }

}
