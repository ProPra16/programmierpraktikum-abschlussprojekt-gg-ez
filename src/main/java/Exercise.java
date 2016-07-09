import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.nio.file.Path;
import java.util.HashMap;



public class Exercise {

    private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder builder = factory.newDocumentBuilder();

    private Document doc;
    private Path path;

    private String name;
    private Element root;
    private Node description;
    private Node classes;
    private Node tests;

    private HashMap<String, String> classMap;
    private HashMap<String, String> testMap;

    /*
     * This Constructor creates an empty Exercise
     * <?xml version="1.0" encoding="UTF-8" standalone="no"?>
     * <excercise>
     *     <description/>
     *     <classes/>
     *     <tests/>
     * </excercise>
     */
    public Exercise(Path path, String name) throws ParserConfigurationException {
        this.path = path;
        this.doc  = builder.newDocument();
        this.root = doc.createElement("exercise");
        this.root.setAttribute("name",name);
        this.name = name;

        doc.appendChild(root);

        this.classes     = doc.createElement("classes");
        this.tests       = doc.createElement("tests");
        this.description = doc.createElement("description");

        this.classMap = new HashMap<>();
        this.testMap  = new HashMap<>();

        root.appendChild(description);
        root.appendChild(classes);
        root.appendChild(tests);
    }

    /*
     * This Constructor loads an Exercise from a Path
     */
    public Exercise(Path path) throws ParserConfigurationException {
        try {
            this.path = path;
            this.doc = builder.parse(path.toFile());
            this.root = doc.getDocumentElement();
            this.name = root.getAttribute("name");

            NodeList tmp = root.getElementsByTagName("classes");
            this.classes = tmp.item(0);
            this.classMap = createMap(classes);

            tmp = root.getElementsByTagName("tests");
            this.tests = tmp.item(0);
            this.testMap = createMap(tests);

            tmp = root.getElementsByTagName("description");
            this.description = tmp.item(0);
        } catch (Exception e) {

        }
    }

    private HashMap<String,String> createMap(Node type) {
        NodeList classList = type.getChildNodes();
        HashMap<String, String> tempMap = new HashMap<>();

        for(int i = 0; i < classList.getLength(); i++){
            Node node = classList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                tempMap.put(element.getAttribute("name"),element.getTextContent());
            }
        }
        return tempMap;
    }

    public void loadMap(HashMap<String, String> map, boolean type){
        Node parent;
        String name;

        if(type) {
            parent = classes;
            name = "class";
        } else {
            parent = tests;
            name = "test";
        }
        while (parent.hasChildNodes()) {
            parent.removeChild(parent.getFirstChild());
        }

        for (String key: map.keySet()) {
            Element element = doc.createElement(name);
            element.setAttribute("name", key);
            element.setTextContent(map.get(key));

            parent.appendChild(element);
        }
    }

    public static String getDefaultClassString(String name){
        return "public class " + name + " {\n" +
                "}";
    }

    public static String getDefaultTestString(String name){
        return "import static org.junit.Assert.*;\n" +
                "import org.junit.Test;\n\n" +
                "public class " + name + "Test {\n\n" +
                "    @Test\n" +
                "    public void testSomething() {\n\n" +
                "    }\n" +
                "}";
    }

    public void addDefaultPair(String namePair) {
        addPair(namePair, getDefaultClassString(namePair), getDefaultTestString(namePair+"Test"));
    }

    public void addPair(String namePair, String classText, String testText) {
        Element classNode = doc.createElement("class");
        classNode.setAttribute("name", namePair);
        classNode.setTextContent(classText);
        this.classes.appendChild(classNode);

        Element testNode = doc.createElement("test");
        testNode.setAttribute("name", namePair+"Test");
        testNode.setTextContent(testText);
        this.tests.appendChild(testNode);
    }

    public void addDefaultClass(String nameClass) {
        addClass(nameClass, getDefaultClassString(nameClass));
    }

    /*
     * Fügt eine class mit dem übergebenen Namen und Text hinzu
     */
    public void addClass(String nameClass, String text) {
        Element classNode = doc.createElement("class");
        classNode.setAttribute("name", nameClass);
        classNode.setTextContent(text);

        this.classes.appendChild(classNode);
    }

    public void addDefaultTest(String nameTest) {
        addTest(nameTest, getDefaultTestString(nameTest));
    }

    /*
     * Fügt einen test mit dem übergebenen Namen und Text hinzu
     */
    public void addTest(String nameTest, String text) {
        Element testNode = doc.createElement("test");
        testNode.setAttribute("name", nameTest);
        testNode.setTextContent(text);

        this.tests.appendChild(testNode);
    }

    /*
     * Setters:
     */
    public void setMaps(HashMap<String, String> classMap, HashMap<String, String> testMap) {
        setClassMap(classMap);
        setTestMap(testMap);
    }

    public void setClassMap(HashMap<String, String> classMap) {
        this.classMap = classMap;
        loadMap(classMap, true);
    }

    public void setTestMap(HashMap<String, String> testMap) {
        this.testMap = testMap;
        loadMap(testMap, false);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public void setDescriptionText(String desc) {
        description.setTextContent(desc);
    }


    /*
     * Getters:
     */
    public String getDescriptionText() {
        return description.getTextContent();
    }

    public HashMap<String, String> getClassMap(){
        return classMap;
    }

    public HashMap<String, String> getTestMap(){
        return testMap;
    }

    public String getName() {
        return name;
    }

    public Document getDoc() {
        return doc;
    }

    public Path getPath() {
        return path;
    }
}
