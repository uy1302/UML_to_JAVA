//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.util.*;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class StyleParser {
//    private String diXml;
//    private Map<String, Object> styleTree;
//
//    public StyleParser(String diXml) {
//        this.diXml = diXml;
//        this.styleTree = null;
//    }
//
//    public Map<String, Object> convertToStyleTree() {
//        System.out.println("<<< CONVERTING XML TO STYLE TREE >>>");
//
//        try {
//            styleTree = new HashMap<>();
//            Document graphModel = Jsoup.parse(diXml);
//            Element root = graphModel.selectFirst("root");
//
//            if (root == null) {
//                System.err.println("No <root> element found in the XML.");
//                return null;
//            }
//
//            Iterator<Element> rootChildren = root.children().iterator();
//            String grandparent = null;
//            String rootParent = null;
//
//            List<Element> relationshipList = new ArrayList<>();
//
//            while (rootChildren.hasNext()) {
////            	System.out.println("$$$$");
//                Element child = rootChildren.next();
//                Map<String, String> childAttrs = child.attributes().dataset();
//                System.out.print(childAttrs.containsKey("parent"));
//                if (childAttrs.containsKey("parent")) {
//                    String parentId = childAttrs.get("parent");
//                    if (parentId.equals(grandparent)) {
//                        rootParent = childAttrs.get("id");
//                        System.out.println(rootParent);
//                        styleTree.put("root", addRootParent(childAttrs));
//                    } else if (childAttrs.containsKey("source") || childAttrs.containsKey("target")) {
//                        if (!childAttrs.containsKey("source")) {
//                            System.err.println("'source' not present in " + childAttrs.get("id") + " relationship");
//                        } else if (!childAttrs.containsKey("target")) {
//                            System.err.println("'target' not present in " + childAttrs.get("id") + " relationship");
//                        } else {
//                            relationshipList.add(child);
//                        }
//                    } else {
//                        Map<String, Object> rootMap = (Map<String, Object>) styleTree.get("root");
//                        if (rootMap != null) {
//                            Map<String, Object> cells = (Map<String, Object>) rootMap.get("cells");
//                            if (cells == null) {
//                                cells = new LinkedHashMap<>();
//                                rootMap.put("cells", cells);
//                            }
//                            cells.put(childAttrs.get("id"), addCells(childAttrs, rootParent));
//                        }
//                    }
//                } else {
//                    if (grandparent == null) {
//                        grandparent = childAttrs.get("id");
//                    }
//                }
//            }
//
//            // Process relationships
//            for (Element child : relationshipList) {
//                Map<String, String> childAttrs = child.attributes().dataset();
//                Map<String, Object> rootMap = (Map<String, Object>) styleTree.get("root");
//                if (rootMap != null) {
//                    Map<String, Object> relationships = (Map<String, Object>) rootMap.get("relationships");
//                    if (relationships == null) {
//                        relationships = new HashMap<>();
//                        rootMap.put("relationships", relationships);
//                    }
//                    relationships.put(childAttrs.get("id"), addRelationships(childAttrs, rootParent));
//                }
//            }
//            System.out.println(styleTree);
//            return styleTree;
//
//        } catch (Exception e) {
//            System.err.println("StyleParser.convertToStyleTree ERROR: " + e.getMessage());
//            return null;
//        }
//    }
//
//    private Map<String, Object> addRootParent(Map<String, String> attrs) {
//        Map<String, Object> rootParent = new HashMap<>();
//        rootParent.put("id", attrs.get("id"));
//        rootParent.put("parent_id", attrs.get("parent"));
//        rootParent.put("cells", new LinkedHashMap<>()); // Keep insertion order for properties and methods
//        rootParent.put("relationships", new HashMap<>());
//        return rootParent;
//    }
//
//    private Map<String, Object> addRelationships(Map<String, String> attrs, String rootParent) {
//        String source = attrs.get("source");
//        String parentSource = (String) ((Map<String, Object>) ((Map<String, Object>) ((Map<String, Object>) styleTree.get("root")).get("cells")).get(source)).get("parent_id");
//
//        while (!parentSource.equals(rootParent)) {
//            source = parentSource;
//            parentSource = (String) ((Map<String, Object>) ((Map<String, Object>) ((Map<String, Object>) styleTree.get("root")).get("cells")).get(source)).get("parent_id");
//        }
//
//        String target = attrs.get("target");
//        String parentTarget = (String) ((Map<String, Object>) ((Map<String,Object>) ((Map<String, Object>) styleTree.get("root")).get("cells")).get(target)).get("parent_id");
//
//        while (!parentTarget.equals(rootParent)) {
//            target = parentTarget;
//            parentTarget = (String) ((Map<String, Object>) ((Map<String, Object>) ((Map<String, Object>) styleTree.get("root")).get("cells")).get(target)).get("parent_id");
//        }
//
//        Map<String, Object> relationship = new HashMap<>();
//        relationship.put("id", attrs.get("id"));
//        relationship.put("parent_id", attrs.get("parent"));
//        relationship.put("source", source);
//        relationship.put("target", target);
//        relationship.put("style", getStyle(attrs.get("style")));
//
//        return relationship;
//    }
//
//    private Map<String, Object> addCells(Map<String, String> attrs, String rootParent) {
//        Map<String, Object> cell = new HashMap<>();
//        cell.put("id", attrs.get("id"));
//        cell.put("parent_id", attrs.get("parent"));
//        cell.put("style", getStyle(attrs.get("style")));
//
//        String value = attrs.get("value");
//        if (!cell.get("style").toString().contains("type") && attrs.get("parent").equals(rootParent)) {
//            ((Map<String, Object>) cell.get("style")).put("type", "html");
//            String[] splitValues = value.replaceAll("<hr .*?>", "\n<hr>\n").strip().split("\n");
//            List<String> valuesList = new ArrayList<>();
//            for (String val : splitValues) {
//                if (!val.equals("<hr>")) {
//                    valuesList.addAll(getTextValues(val));
//                }
//            }
//            cell.put("values", valuesList);
//        } else {
//            cell.put("values", getTextValues(value));
//        }
//
//        return cell;
//    }
//
//    private List<String> getTextValues(String values) {
//        List<String> vals = new ArrayList<>();
//        StringBuilder tempVal = new StringBuilder();
//
//        for (char v : values.toCharArray()) {
//            if (v == '+' || v == '-' || v == '#') {
//                if (!tempVal.toString().isEmpty()) {
//                    vals.add(tempVal.toString().trim().replace(" ", ""));
//                }
//                tempVal.setLength(0);
//            }
//            tempVal.append(v);
//        }
//
//        vals.add(tempVal.toString().trim().replace(" ", ""));
//        return vals;
//    }
//
//    private Map<String, String> getStyle(String styleAttrs) {
//        Map<String, String> styleDict = new HashMap<>();
//        String[] styleList = styleAttrs.split(";");
//
//        for (String s : styleList) {
//            if (s.contains("=")) {
//                String[] sList = s.split("=");
//                styleDict.put(sList[0], sList[1]);
//            } else if (!s.isEmpty()) {
//                styleDict.put("type", s);
//            }
//        }
//
//        return styleDict;
//    }
//}


//import org.w3c.dom.*;
//import javax.xml.parsers.*;
//import java.io.*;
//import java.util.*;
//import java.util.regex.*;
//
//public class StyleParser {
//
//    private String diXml;
//    private Map<String, Object> styleTree;
//
//    public StyleParser(String diXml) {
//        this.diXml = diXml;
//        this.styleTree = new HashMap<>();
//    }
//
//    public Map<String, Object> convertToStyleTree() {
//        System.out.println("<<< CONVERTING XML TO STYLE TREE >>>");
//
//        try {
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            Document doc = builder.parse(new ByteArrayInputStream(diXml.getBytes()));
//
//            Node root = doc.getDocumentElement().getElementsByTagName("root").item(0);
//            NodeList rootChildren = root.getChildNodes();
//
//            String grandparent = null;
//            String rootParent = null;
//            List<Map<String, String>> relationshipList = new ArrayList<>();
//
//            for (int i = 0; i < rootChildren.getLength(); i++) {
//                Node child = rootChildren.item(i);
//                if (child.getNodeType() == Node.ELEMENT_NODE) {
//                    Element childElement = (Element) child;
//                    NamedNodeMap childAttrs = child.getAttributes();
//
//                    if (childAttrs.getNamedItem("parent") != null) {
//                        String parent = childAttrs.getNamedItem("parent").getNodeValue();
//                        if (parent.equals(grandparent)) { // Root parent element
//                            rootParent = childAttrs.getNamedItem("id").getNodeValue();
//                            styleTree.put("root", addRootParent(childAttrs));
//                        } else if (childAttrs.getNamedItem("source") != null || childAttrs.getNamedItem("target") != null) { // Relationship
//                            if (childAttrs.getNamedItem("source") == null) {
//                                System.out.println("'source' not present in relationship " + childAttrs.getNamedItem("id").getNodeValue());
//                            } else if (childAttrs.getNamedItem("target") == null) {
//                                System.out.println("'target' not present in relationship " + childAttrs.getNamedItem("id").getNodeValue());
//                            } else {
//                                relationshipList.add(attributesToMap(childAttrs));
//                            }
//                        } else { // Cell element
//                            Map<String, Object> rootMap = (Map<String, Object>) styleTree.get("root");
//                            Map<String, Object> cells = (Map<String, Object>) rootMap.get("cells");
//                            cells.put(childAttrs.getNamedItem("id").getNodeValue(), addCells(childAttrs, rootParent));
//                        }
//                    } else { // Grandparent element
//                        if (grandparent == null) {
//                            grandparent = childAttrs.getNamedItem("id").getNodeValue();
//                        }
//                    }
//                }
//            }
//
//            // Process relationships at the end to resolve source and target
//            for (Map<String, String> childAttrs : relationshipList) {
//                Map<String, Object> rootMap = (Map<String, Object>) styleTree.get("root");
//                Map<String, Object> relationships = (Map<String, Object>) rootMap.get("relationships");
//                relationships.put(childAttrs.get("id"), addRelationships(childAttrs, rootParent));
//            }
//
//            return styleTree;
//        } catch (Exception e) {
//            System.out.println("StyleParser.convertToStyleTree ERROR: " + e.getMessage());
//            return null;
//        }
//    }
//
//    private Map<String, Object> addRootParent(NamedNodeMap attrs) {
//        Map<String, Object> rootParentDict = new HashMap<>();
//        rootParentDict.put("id", attrs.getNamedItem("id").getNodeValue());
//        rootParentDict.put("parent_id", attrs.getNamedItem("parent").getNodeValue());
//        rootParentDict.put("cells", new LinkedHashMap<String, Object>()); // Use LinkedHashMap for ordering
//        rootParentDict.put("relationships", new HashMap<String, Object>());
//        return rootParentDict;
//    }
//
//    private Map<String, Object> addRelationships(Map<String, String> attrs, String rootParent) {
//        // Similar to Python logic
//        String source = attrs.get("source");
//        String target = attrs.get("target");
//
//        // Logic to resolve parent sources and targets...
//
//        Map<String, Object> relationships = new HashMap<>();
//        relationships.put("id", attrs.get("id"));
//        relationships.put("parent_id", attrs.get("parent"));
//        relationships.put("source", source);
//        relationships.put("target", target);
//        relationships.put("style", getStyle(attrs.get("style")));
//
//        return relationships;
//    }
//
//    private Map<String, Object> addCells(NamedNodeMap attrs, String rootParent) {
//        Map<String, Object> cellResult = new HashMap<>();
//        Map<String, String> style = getStyle(attrs.getNamedItem("style").getNodeValue());
//
//        cellResult.put("id", attrs.getNamedItem("id").getNodeValue());
//        cellResult.put("parent_id", attrs.getNamedItem("parent").getNodeValue());
//        cellResult.put("style", style);
//
//        String value = attrs.getNamedItem("value").getNodeValue();
//        if (!style.containsKey("type") && rootParent.equals(attrs.getNamedItem("parent").getNodeValue())) {
//            style.put("type", "html");
//            cellResult.put("values", splitHtmlValues(value));
//        } else {
//            cellResult.put("values", getTextValues(value));
//        }
//
//        return cellResult;
//    }
//
//    private List<String> splitHtmlValues(String value) {
//        List<String> result = new ArrayList<>();
//        String[] splitValues = value.replaceAll("<hr .*?>", "\n<hr>\n").split("\n");
//        for (String val : splitValues) {
//            if (!val.equals("<hr>")) {
//                result.addAll(getTextValues(val));
//            }
//        }
//        return result;
//    }
//
//    private List<String> getTextValues(String value) {
//        List<String> vals = new ArrayList<>();
//        StringBuilder tempVal = new StringBuilder();
//
//        for (char c : value.toCharArray()) {
//            if (c == '+' || c == '-' || c == '#') {
//                if (tempVal.length() > 0) {
//                    vals.add(tempVal.toString().trim());
//                }
//                tempVal = new StringBuilder();
//            }
//            tempVal.append(c);
//        }
//
//        if (tempVal.length() > 0) {
//            vals.add(tempVal.toString().trim());
//        }
//
//        return vals;
//    }
//
//    private Map<String, String> getStyle(String styleAttrs) {
//        Map<String, String> styleDict = new HashMap<>();
//        String[] styleList = styleAttrs.split(";");
//
//        for (String s : styleList) {
//            if (s.contains("=")) {
//                String[] sList = s.split("=");
//                styleDict.put(sList[0], sList[1]);
//            } else if (!s.isEmpty()) {
//                styleDict.put("type", s);
//            }
//        }
//
//        return styleDict;
//    }
//
//    private Map<String, String> attributesToMap(NamedNodeMap attrs) {
//        Map<String, String> map = new HashMap<>();
//        for (int i = 0; i < attrs.getLength(); i++) {
//            Node item = attrs.item(i);
//            map.put(item.getNodeName(), item.getNodeValue());
//        }
//        return map;
//    }
//}
package Parser;




import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StyleParser {
    private String diXml;
    private Map<String, Object> styleTree;

    /**
     * Constructor for StyleParser
     * 
     * @param diXml the decoded and decompressed DrawIO XML
     */
    public StyleParser(String diXml) {
        this.diXml = diXml;
        this.styleTree = null;
    }

    /**
     * Convert the XML to a style tree
     * 
     * @return the style tree as a Map, or null if conversion fails
     */
    public Map<String, Object> convertToStyleTree() {
        System.out.println("<<< CONVERTING XML TO STYLE TREE >>>");

        try {
            styleTree = new HashMap<>();
            Document graphModel = Jsoup.parse(diXml, "", org.jsoup.parser.Parser.xmlParser());
            Element root = graphModel.select("root").first();
            Elements rootChildren = root.children();

            String grandparent = null;
            String rootParent = null;
            List<Element> relationshipList = new ArrayList<>();

            for (Element child : rootChildren) {
                Map<String, String> childAttrs = getElementAttributes(child);

                if (childAttrs.containsKey("parent")) {
                    if (childAttrs.get("parent").equals(grandparent)) {
                        // Found the root parent element
                        rootParent = childAttrs.get("id");
                        styleTree.put("root", addRootParent(childAttrs));
                    } else if (childAttrs.containsKey("source") || childAttrs.containsKey("target")) {
                        // Found a relationship element
                        if (!childAttrs.containsKey("source")) {
                            System.out.printf("'source' not present in %s relationship%n", childAttrs.get("id"));
                        } else if (!childAttrs.containsKey("target")) {
                            System.out.printf("'target' not present in %s relationship%n", childAttrs.get("id"));
                        } else {
                            relationshipList.add(child);
                        }
                    } else {
                        // Found a cell element
                        Map<String, Object> rootMap = (Map<String, Object>) styleTree.get("root");
                        Map<String, Object> cells = (Map<String, Object>) rootMap.get("cells");
                        cells.put(childAttrs.get("id"), addCells(childAttrs, rootParent));
                    }
                } else {
                    // Found the grandparent element
                    if (grandparent == null) {
                        grandparent = childAttrs.get("id");
                    }
                }
            }

            // Process relationships at the end
            Map<String, Object> rootMap = (Map<String, Object>) styleTree.get("root");
            Map<String, Object> relationships = (Map<String, Object>) rootMap.get("relationships");
            for (Element child : relationshipList) {
                Map<String, String> childAttrs = getElementAttributes(child);
                relationships.put(childAttrs.get("id"), addRelationships(childAttrs, rootParent));
            }

            return styleTree;
        } catch (Exception e) {
            System.out.printf("StyleParser.convertToStyleTree ERROR: %s%n", e.getMessage());
            return null;
        }
    }

    /**
     * Format dictionary for the root parent
     * 
     * @param attrs the root_parent element attributes
     * @return root parent dictionary
     */
    private Map<String, Object> addRootParent(Map<String, String> attrs) {
        Map<String, Object> rootParentDict = new HashMap<>();
        rootParentDict.put("id", attrs.get("id"));
        rootParentDict.put("parent_id", attrs.get("parent"));
        rootParentDict.put("cells", new LinkedHashMap<>()); // Use LinkedHashMap to maintain insertion order
        rootParentDict.put("relationships", new HashMap<>());
        return rootParentDict;
    }

    /**
     * Format dictionary for the relationships
     * 
     * @param attrs the relationship element attributes
     * @param rootParent the id of the root parent element
     * @return relationships dictionary
     */
    private Map<String, Object> addRelationships(Map<String, String> attrs, String rootParent) {
        Map<String, Object> rootMap = (Map<String, Object>) styleTree.get("root");
        Map<String, Object> cells = (Map<String, Object>) rootMap.get("cells");

        String source = attrs.get("source");
        Map<String, Object> sourceCell = (Map<String, Object>) cells.get(source);
        String parentSource = (String) sourceCell.get("parent_id");
        while (!parentSource.equals(rootParent)) {
            source = parentSource;
            sourceCell = (Map<String, Object>) cells.get(source);
            parentSource = (String) sourceCell.get("parent_id");
        }

        String target = attrs.get("target");
        Map<String, Object> targetCell = (Map<String, Object>) cells.get(target);
        String parentTarget = (String) targetCell.get("parent_id");
        while (!parentTarget.equals(rootParent)) {
            target = parentTarget;
            targetCell = (Map<String, Object>) cells.get(target);
            parentTarget = (String) targetCell.get("parent_id");
        }

        Map<String, String> style = getStyle(attrs.get("style"));

        Map<String, Object> relationshipDict = new HashMap<>();
        relationshipDict.put("id", attrs.get("id"));
        relationshipDict.put("parent_id", attrs.get("parent"));
        relationshipDict.put("source", source);
        relationshipDict.put("target", target);
        relationshipDict.put("style", style);

        return relationshipDict;
    }

    /**
     * Format dictionary for the cells
     * 
     * @param attrs the cell element attributes
     * @param rootParent the id of the root parent
     * @return cell dictionary
     */
    private Map<String, Object> addCells(Map<String, String> attrs, String rootParent) {
        Map<String, String> style = getStyle(attrs.get("style"));
        String value = attrs.get("value");

        Map<String, Object> cellResult = new HashMap<>();
        cellResult.put("id", attrs.get("id"));
        cellResult.put("parent_id", attrs.get("parent"));
        cellResult.put("style", style);

        if (!style.containsKey("type") && attrs.get("parent").equals(rootParent)) {
            style.put("type", "html");
            String[] splitValues = value.replaceAll("<hr .*?>", "\n<hr>\n").split("\n");
            List<String> processedValues = new ArrayList<>();
            for (String val : splitValues) {
                if (!val.equals("<hr>")) {
                    processedValues.add(getTextValues(Jsoup.parse(val, "", org.jsoup.parser.Parser.xmlParser()).text()).get(0));
                }
            }
            cellResult.put("values", processedValues);
            style.put("type", "html");
        } else {
            cellResult.put("values", getTextValues(value));
        }

        return cellResult;
    }

    /**
     * Get individual values for the joined values
     * 
     * @param values raw values for extraction
     * @return list of final values
     */
    private List<String> getTextValues(String values) {
        List<String> vals = new ArrayList<>();
        StringBuilder tempVal = new StringBuilder();

        for (char v : values.toCharArray()) {
            if (v == '+' || v == '-' || v == '#') {
                if (tempVal.length() > 0) {
//                    vals.add(tempVal.toString().strip().replace(" ", ""));
                	vals.add(tempVal.toString().trim().replace(" ", ""));
                    tempVal = new StringBuilder();
                }
            }
            tempVal.append(v);
        }

//        vals.add(tempVal.toString().strip().replace(" ", ""));
        vals.add(tempVal.toString().trim().replace(" ", ""));
        return vals;
    }

    /**
     * Convert the style attribute to a dictionary
     * 
     * @param styleAttrs style attributes of the element
     * @return style attribute as a dictionary
     */
    private Map<String, String> getStyle(String styleAttrs) {
        Map<String, String> styleDict = new HashMap<>();
        String[] styleList = styleAttrs.split(";");

        for (String s : styleList) {
            if (s.contains("=")) {
                String[] sList = s.split("=");
                styleDict.put(sList[0], sList[1]);
            } else {
                if (!s.isEmpty()) {
                    styleDict.put("type", s);
                }
            }
        }

        return styleDict;
    }

    /**
     * Extract attributes from an HTML element
     * 
     * @param element Jsoup Element
     * @return Map of attributes
     */
    private Map<String, String> getElementAttributes(Element element) {
        Map<String, String> attrs = new HashMap<>();
        for (org.jsoup.nodes.Attribute attr : element.attributes()) {
            attrs.put(attr.getKey(), attr.getValue());
        }
        return attrs;
    }

    // Optional: Add a main method for testing
    public static void main(String[] args) {
        String xmlContent = "your XML content here";
        StyleParser parser = new StyleParser(xmlContent);
        Map<String, Object> styleTree = parser.convertToStyleTree();
        
        if (styleTree != null) {
            System.out.println("Style Tree successfully created.");
            // Add additional print or processing logic here
        }
    }
}





