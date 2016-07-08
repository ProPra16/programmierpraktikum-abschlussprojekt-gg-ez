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
import java.util.HashMap;



public class Exercise {

    private File file;
    private Document doc;

    private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder builder = factory.newDocumentBuilder();

    private String path;
    private String name;
    private Element root;
    private Node description;
    private Node classes;
    private Node tests;

    /*
     * Dieser Construktor erstellt eine leere XML Datei mit dem Aufbau:
     *
     * <?xml version="1.0" encoding="UTF-8" standalone="no"?>
     * <excercise>
     *     <description/>
     *     <classes/>
     *     <tests/>
     * </excercise>
     *
     * Die Funktionen addDefaultPair und addPair fügen eine class & test paar hinzu
     */
    public Exercise(String name, String path) throws ParserConfigurationException {
        this.name = name;
        this.path = path;
        this.doc  = builder.newDocument();
        this.root = doc.createElement("exercise");
        this.root.setAttribute("name",name);

        doc.appendChild(root);

        this.classes     = doc.createElement("classes");
        this.tests       = doc.createElement("tests");
        this.description = doc.createElement("description");

        root.appendChild(description);
        root.appendChild(classes);
        root.appendChild(tests);
    }

    /*
     * Konstruktor liest eine XML Datei ein
     */
    public Exercise(File file) throws ParserConfigurationException, IOException, SAXException {
        this.file = file;
        this.path = file.getPath();
        loadEx();
    }

    private void loadEx() throws ParserConfigurationException, IOException, SAXException {
        this.doc = builder.parse(file);
        this.root = doc.getDocumentElement();
        this.name = root.getAttribute("name");

        NodeList tmp = root.getElementsByTagName("classes");
        this.classes = tmp.item(0);

        tmp = root.getElementsByTagName("tests");
        this.tests = tmp.item(0);

        tmp = root.getElementsByTagName("description");
        this.description = tmp.item(0);
    }

    public void saveEx() throws TransformerException {
        System.out.println("saved");
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        DOMSource src = new DOMSource(doc);

        this.file = new File(path);

        StreamResult fileResult = new StreamResult(file);
        transformer.transform(src, fileResult);
    }

    public void addDefaultPair(String namePair) throws TransformerException {
        addPair(namePair,
                        "public class " + namePair + " {\n" +
                        "}"
                ,
                        "import static org.junit.Assert.*;\n" +
                        "import org.junit.Test;\n\n" +
                        "public class " + namePair + "Test {\n\n" +
                        "    @Test\n" +
                        "    public void testSomething() {\n\n" +
                        "    }\n" +
                        "}"
        );
    }

    public void addPair(String namePair, String classText, String testText) throws TransformerException {
        Element classNode = doc.createElement("class");
        classNode.setAttribute("name", namePair);
        classNode.setTextContent(classText);
        this.classes.appendChild(classNode);

        Element testNode = doc.createElement("test");
        testNode.setAttribute("name", namePair+"Test");
        testNode.setTextContent(testText);
        this.tests.appendChild(testNode);
    }

    public void addDefaultClass(String nameClass) throws TransformerException, ParserConfigurationException {
        addClass(nameClass,
                        "public class " + nameClass + " {\n" +
                        "}"
        );
    }

    /*
     * Fügt eine class mit dem übergebenen Namen und Text hinzu
     */
    public void addClass(String nameClass, String text) throws ParserConfigurationException, TransformerException {
        Element classNode = doc.createElement("class");
        classNode.setAttribute("name", nameClass);
        classNode.setTextContent(text);

        this.classes.appendChild(classNode);
    }

    public void addDefaultTest(String nameTest) throws TransformerException, ParserConfigurationException {
        addTest(nameTest,
                        "import static org.junit.Assert.*;\n" +
                        "import org.junit.Test;\n\n" +
                        "public class " + nameTest + "Test {\n\n" +
                        "    @Test\n" +
                        "    public void testSomething() {\n\n" +
                        "    }\n" +
                        "}"
        );
    }

    /*
     * Fügt einen test mit dem übergebenen Namen und Text hinzu
     */
    public void addTest(String nameTest, String text) throws ParserConfigurationException, TransformerException {
        Element testNode = doc.createElement("test");
        testNode.setAttribute("name", nameTest);
        testNode.setTextContent(text);

        this.tests.appendChild(testNode);
    }

    public void deleteClass(String nameClass){
        NodeList classesChildNodesNodes = classes.getChildNodes();
        for(int i = 0; i < classesChildNodesNodes.getLength(); i++){
            Node node = classesChildNodesNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if(element.getAttribute("name").equals(nameClass)){
                    classes.removeChild(element);
                }
            }
        }
    }

    public void deleteTest(String nameTest){
        NodeList classesChildNodesNodes = tests.getChildNodes();
        for(int i = 0; i < classesChildNodesNodes.getLength(); i++){
            Node node = classesChildNodesNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if(element.getAttribute("name").equals(nameTest)){
                    tests.removeChild(element);
                }
            }
        }
    }

    /*
     * Gibt eine HashMap mit Strings der classes zurück
     * Ein String in der Liste = eine class
     */
    public HashMap<String, String> getClassesText(){
        NodeList classList = classes.getChildNodes();
        HashMap<String, String> classText = new HashMap<>();

        for(int i = 0; i < classList.getLength(); i++){
            Node node = classList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                classText.put(element.getAttribute("name"),element.getTextContent());
            }
        }

        return classText;
    }

    /*
     * Gibt eine HashMap mit Strings der tests zurück
     * Ein String in der Liste = ein test
     */
    public HashMap<String, String> getTestsText(){
        NodeList testList = tests.getChildNodes();
        HashMap<String, String> testText = new HashMap<>();

        for(int i = 0; i < testList.getLength(); i++){
            Node node = testList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                testText.put(element.getAttribute("name"),element.getTextContent());
            }
        }

        return testText;
    }

    public void updateClass(String name, String text){
        NodeList classList = classes.getChildNodes();

        for(int i = 0; i < classList.getLength(); i++) {
            Node node = classList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if(element.getAttribute("name").equals(name)) {
                    node.setTextContent(text);
                }
            }
        }
    }

    public void updateTest(String name, String text){
        NodeList testList = tests.getChildNodes();

        for(int i = 0; i < testList.getLength(); i++) {
            Node node = testList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if(element.getAttribute("name").equals(name)) {
                    node.setTextContent(text);
                }
            }
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void addDescriptionText(String desc) throws TransformerException {
        description.setTextContent(desc);
    }

    public String getDescriptionText() {
        return description.getTextContent();
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }
}
