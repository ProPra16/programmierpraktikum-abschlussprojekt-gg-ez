import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.nio.file.Path;
import java.util.HashMap;

/*
 * How to use:
 * -adding/removing classes/tests:
 *   1. Add the class/test to the map of classes/tests (in MainController)
 *   2. use setMaps(map) || setClassMap(map) || setTestMap(map) to update the classes/maps from the HashMap
 *
 * -changing the save location (and also file name):
 *   set the new path with setPath(path)
 *
 * -changing exercise name (not file name):
 *   set the new name with setName(name);
 */

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
     *
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
            this.classMap = initializeMaps(classes);

            tmp = root.getElementsByTagName("tests");
            this.tests = tmp.item(0);
            this.testMap = initializeMaps(tests);

            tmp = root.getElementsByTagName("description");
            this.description = tmp.item(0);
        } catch (Exception e) {

        }
    }

    private HashMap<String,String> initializeMaps(Node type) {
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

    public void importMap(HashMap<String, String> map, boolean type){
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

    public boolean isEmptyClass(){
        for(String key: classMap.keySet()){
            if(!classMap.get( key ).equals( getDefaultClassString(key) ) ){
                return false;
            }
        }
        return true;
    }

    public static String getDefaultClassString(String name){
        return "public class " + name + " {\n" +
                "}";
    }

    public static String getDefaultTestString(String name){
        return "import static org.junit.Assert.*;\n" +
                "import org.junit.Test;\n\n" +
                "public class " + name + " {\n\n" +
                "    @Test\n" +
                "    public void testSomething() {\n\n" +
                "    }\n" +
                "}";
    }

    /*
     * Setters:
     */
    public void setPath(Path path) {
        this.path = path;
    }

    public void setName(String name) {
        this.name = name;
        this.root.setAttribute("name", this.name);
    }

    public void setDescriptionText(String desc) {
        description.setTextContent(desc);
    }

    public void setMaps(HashMap<String, String> classMap, HashMap<String, String> testMap) {
        setClassMap(classMap);
        setTestMap(testMap);
    }

    public void setClassMap(HashMap<String, String> classMap) {
        this.classMap = classMap;
        importMap(classMap, true);
    }

    public void setTestMap(HashMap<String, String> testMap) {
        this.testMap = testMap;
        importMap(testMap, false);
    }


    /*
     * Getters:
     */
    public Path getPath() {
        return path;
    }

    public Document getDoc() {
        return doc;
    }

    public String getName() {
        return name;
    }

    public String getDescriptionText() {
        return description.getTextContent();
    }

    public HashMap<String, String> getClassMap(){
        return classMap;
    }

    public HashMap<String, String> getTestMap(){
        return testMap;
    }
}
