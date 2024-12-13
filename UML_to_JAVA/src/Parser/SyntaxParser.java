package Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyntaxParser {
    /**
     * Parse the style tree into the syntax tree
     *
     * @param styleTree style tree of the drawio file
     */
    private Map<String, Object> styleTree;
    private List<String> classSLst;

    public SyntaxParser(Map<String, Object> styleTree) {
        this.styleTree = styleTree;
        this.classSLst = new ArrayList<>(List.of("int", "String", "double", "float", "char", "boolean"));
    }

    /**
     * Convert the style tree to syntax tree
     *
     * @return syntax_tree: the syntax tree that is used by the generators
     */
    public Map<String, Map<String, Object>> convertToSyntaxTree() {
        System.out.println("<<< CONVERTING STYLE TREE TO SYNTAX TREE >>>");

        try {
            Map<String, Map<String, Object>> syntaxTree = new HashMap<>();
            Map<String, Object> cells = (Map<String, Object>) ((Map<String, Object>) styleTree.get("root")).get("cells");
            Map<String, Object> relationships = (Map<String, Object>) ((Map<String, Object>) styleTree.get("root")).get("relationships");
            String parent = (String) ((Map<String, Object>) styleTree.get("root")).get("id");

            boolean propertiesDone = false;
            int _id = 0;

            for (Map.Entry<String, Object> entry : cells.entrySet()) {
                String key = entry.getKey();
                Map<String, Object> value = (Map<String, Object>) entry.getValue();

                if (relationships.containsKey(value.get("parent_id")) || 
                    ((Map<String, Object>) value.get("style")).containsKey("endArrow")) {
                    // skip the label for relationships
                    continue;
                }

                Map<String, Object> style = (Map<String, Object>) value.get("style");
                String styleType = ((String) style.get("type")).toLowerCase();

                if (value.get("parent_id").equals(parent) && 
                    (styleType.equals("swimlane") || styleType.equals("html"))) {
                    // start of a new cell
                    syntaxTree.put(key, createTreeTemplate(value));
                    propertiesDone = false;
                    _id = 0;
                } else {
                    // properties and methods in the cell
                    if (styleType.equals("line") && syntaxTree.containsKey(value.get("parent_id"))) {
                        propertiesDone = true;
                        _id = 0;
                    } else {
                        Map<String, Object> parentCell = (Map<String, Object>) syntaxTree.get(value.get("parent_id"));
                        if (!propertiesDone) {
                            // properties
                            Map<Integer, Map<String, Object>> properties = 
                                (Map<Integer, Map<String, Object>>) parentCell.get("properties");
                            properties.putAll(createPropertiesTemplate(value, _id));
                            _id += ((List<String>) value.get("values")).size();
                        } else {
                            // methods
                            Map<Integer, Map<String, Object>> methods = 
                                (Map<Integer, Map<String, Object>>) parentCell.get("methods");
                            methods.putAll(createMethodsTemplate(value, _id));
                            _id += ((List<String>) value.get("values")).size();
                        }
                    }
                }
            }

            for (String relationship : relationships.keySet()) {
                addRelationships(syntaxTree, (Map<String, Object>) relationships.get(relationship));
            }

            return syntaxTree;
        } catch (Exception e) {
            System.out.println("SyntaxParser.convertToSyntaxTree ERROR: " + e);
            return null;
        }
    }

    /**
     * Create the template that will house each cell
     *
     * @param mainCell the starting, parent cell
     * @return template: the starting template (dictionary)
     */
    private Map<String, Object> createTreeTemplate(Map<String, Object> mainCell) {
        Map<String, Object> template = new HashMap<>();
        template.put("type", "class");
        template.put("name", ((List<String>) mainCell.get("values")).size() > 0 ? 
            ((List<String>) mainCell.get("values")).get(0) : "");
        template.put("properties", new HashMap<Integer, Map<String, Object>>());
        template.put("methods", new HashMap<Integer, Map<String, Object>>());

        Map<String, List<String>> relationships = new HashMap<>();
        relationships.put("implements", new ArrayList<>());
        relationships.put("extends", new ArrayList<>());
        relationships.put("association", new ArrayList<>());
        relationships.put("aggregation", new ArrayList<>());
        relationships.put("composition", new ArrayList<>());
        template.put("relationships", relationships);

        Map<String, Object> style = (Map<String, Object>) mainCell.get("style");
        if (style.get("type").equals("html")) {
            List<String> values = (List<String>) mainCell.get("values");
            if (values.size() > 0) {
                template.put("name", values.get(0));
                template.put("properties", createPropertiesTemplate(
                    Map.of("values", values.size() > 1 ? values.get(1) : null), 0));
                template.put("methods", createMethodsTemplate(
                    Map.of("values", values.size() > 2 ? values.get(2) : null), 0));
            }
        }

        if (style.containsKey("fontStyle") && style.get("fontStyle").equals("2")) {
            template.put("type", "abstract");
        } else if (((String) template.get("name")).toLowerCase().startsWith("<<interface>>")) {
            template.put("type", "interface");
            template.put("name", ((String) template.get("name")).substring(13));
        }

        String name = (String) template.get("name");
        if (!classSLst.contains(name)) {
            classSLst.add(name);
        }

        return template;
    }

    // Add other methods (createPropertiesTemplate, createMethodsTemplate, getAccessModifier, addRelationships)
    // Similar to the createTreeTemplate method, these would need to be fully implemented with Java syntax

    // Placeholder methods with stub implementations
    private Map<Integer, Map<String, Object>> createPropertiesTemplate(Map<String, Object> propertyDict, int _id) {
        // Implement the properties template creation logic
    	List<String> values = (List<String>) propertyDict.get("values");
        Map<Integer, Map<String, Object>> template = new HashMap<>();

        if (values == null) return template;

        for (String val : values) {
            if (val.isEmpty()) continue;

            _id++;
            val = val.strip();
            char accessModifierSymbol = val.charAt(0);
            String[] tempVal = val.substring(1).split(":");

            Map<String, Object> propertyDetails = new HashMap<>();
            propertyDetails.put("access", getAccessModifier(accessModifierSymbol));
            propertyDetails.put("name", tempVal[0].strip());
            propertyDetails.put("type", tempVal[1].strip());

            template.put(_id, propertyDetails);
        }

        return template;
    }

    private Map<Integer, Map<String, Object>> createMethodsTemplate(Map<String, Object> methodDict, int _id) {
    	List<String> values = (List<String>) methodDict.get("values");
        Map<Integer, Map<String, Object>> template = new HashMap<>();

        if (values == null) return template;

        for (String val : values) {
            if (val.isEmpty()) continue;

            _id++;
            val = val.strip();
            char accessModifierSymbol = val.charAt(0);
            String[] tempVal = val.substring(1).split(":");

            // Handle parameters
            if (tempVal[0].contains("(")) {
                int idx = tempVal[0].indexOf('(');
                String param = tempVal[0].substring(idx + 1, tempVal[0].length() - 1);
                List<String> paramList = param.isEmpty() ? new ArrayList<>() : Arrays.asList(param.split(","));

                for (int i = 0; i < paramList.size(); i++) {
                    for (String x : classSLst) {
                        if (paramList.get(i).strip().startsWith(x)) {
                            paramList.set(i, paramList.get(i).replace(x, x + " "));
                        }
                    }
                }

                param = String.join(", ", paramList);
                tempVal[0] = tempVal[0].substring(0, idx + 1) + param + ')';
            }

            Map<String, Object> methodDetails = new HashMap<>();
            methodDetails.put("access", getAccessModifier(accessModifierSymbol));
            methodDetails.put("name", tempVal[0].strip());
            methodDetails.put("return_type", tempVal.length > 1 ? tempVal[1].strip() : "void");

            template.put(_id, methodDetails);
        }

        return template;
    }

    private String getAccessModifier(char symbol) {
        Map<Character, String> accessModifierDict = new HashMap<>();
        accessModifierDict.put('+', "public");
        accessModifierDict.put('#', "protected");
        accessModifierDict.put('-', "private");

        return accessModifierDict.get(symbol);
    }

    private void addRelationships(Map<String, Map<String, Object>> syntaxTree, Map<String, Object> relationship) {
        // Implement the relationships addition logic
    	String source = (String) relationship.get("source");
        String target = (String) relationship.get("target");
        Map<String, String> style = (Map<String, String>) relationship.get("style");

        Map<String, Object> sourceCell = syntaxTree.get(source);
        Map<String, Object> targetCell = syntaxTree.get(target);

        if (style.containsKey("endArrow") && 
            (style.get("endArrow").toLowerCase().equals("block") || 
             style.get("endArrow").toLowerCase().equals("none"))) {
            
            if (style.get("endArrow").toLowerCase().equals("none") || 
                style.get("endFill").toLowerCase().equals("1")) {
                // association
//                ((List<String>) targetCell.get("relationships.association")).add(source);
            	((List<String>) ((Map<String, List<String>>) targetCell.get("relationships")).get("association")).add(source);
            } else if (style.containsKey("dashed") && style.get("dashed").equals("1")) {
                // implements
//                ((List<String>) sourceCell.get("relationships.implements")).add(target);
                ((List<String>) ((Map<String, List<String>>) sourceCell.get("relationships")).get("implements")).add(target);


            } else {
                // extends
//                ((List<String>) sourceCell.get("relationships.extends")).add(target);
                ((List<String>) ((Map<String, List<String>>) sourceCell.get("relationships")).get("extends")).add(target);

            }
        } else if ((style.containsKey("endArrow") && 
                    style.get("endArrow").toLowerCase().equals("diamondthin")) || 
                   (style.containsKey("startArrow") && 
                    style.get("startArrow").toLowerCase().equals("diamondthin"))) {
            
            if (style.containsKey("endFill") && style.get("endFill").equals("1")) {
                // composition
                ((List<String>) ((Map<String, List<String>>) targetCell.get("relationships")).get("composition")).add(source);
            } else {
                // aggregation
            	((List<String>) ((Map<String, List<String>>) targetCell.get("relationships")).get("aggregation")).add(source);
            }
        }
    }
}





//import java.util.*;
//
//public class SyntaxParser {
//    /**
//     * Parse the style tree into the syntax tree
//     */
//    private Map<String, Object> styleTree;
//    private List<String> classList;
//
//    /**
//     * Constructor for SyntaxParser
//     * 
//     * @param styleTree style tree of the drawio file
//     */
//    public SyntaxParser(Map<String, Object> styleTree) {
//        this.styleTree = styleTree;
//        this.classList = new ArrayList<>(Arrays.asList("int", "String", "double", "float", "char", "boolean"));
//    }
//
//    /**
//     * Convert the style tree to syntax tree
//     * 
//     * @return syntax tree used by the generators
//     */
//    public Map<String, Map<String, Object>> convertToSyntaxTree() {
//        System.out.println("<<< CONVERTING STYLE TREE TO SYNTAX TREE >>>");
//
//        try {
//            Map<String, Map<String, Object>> syntaxTree = new HashMap<>();
//            Map<String, Object> rootMap = (Map<String, Object>) styleTree.get("root");
//            Map<String, Object> cells = (Map<String, Object>) rootMap.get("cells");
//            Map<String, Object> relationships = (Map<String, Object>) rootMap.get("relationships");
//            String parent = (String) rootMap.get("id");
//
//            boolean propertiesDone = false;
//            int _id = 0;
//
//            for (Map.Entry<String, Object> entry : cells.entrySet()) {
//                String key = entry.getKey();
//                Map<String, Object> value = (Map<String, Object>) entry.getValue();
//
//                // Skip labels for relationships
//                if (relationships.containsKey(value.get("parent_id")) || 
//                    ((Map<String, String>)value.get("style")).containsKey("endArrow")) {
//                    continue;
//                }
//
//                // Start of a new cell
//                if (value.get("parent_id").equals(parent) && 
//                    (((Map<String, String>)value.get("style")).get("type").toLowerCase().equals("swimlane") || 
//                     ((Map<String, String>)value.get("style")).get("type").toLowerCase().equals("html"))) {
//                    syntaxTree.put(key, treeTemplate(value));
//                    propertiesDone = false;
//                    _id = 0;
//                } else {
//                    // Properties and methods in the cell
//                    Map<String, String> style = (Map<String, String>) value.get("style");
//                    if (style.get("type").toLowerCase().equals("line") && 
//                        syntaxTree.containsKey(value.get("parent_id"))) {
//                        propertiesDone = true;
//                        _id = 0;
//                    } else {
//                        Map<String, Object> parentCell = syntaxTree.get(value.get("parent_id"));
//                        List<String> valueList = (List<String>) value.get("values");
//
//                        if (!propertiesDone) {
//                            // Properties
//                            Map<Integer, Object> properties = (Map<Integer, Object>) parentCell.get("properties");
//                            properties.putAll(propertiesTemplate(value, _id));
//                            _id += valueList.size();
//                        } else {
//                            // Methods
//                            Map<Integer, Object> methods = (Map<Integer, Object>) parentCell.get("methods");
//                            methods.putAll(methodsTemplate(value, _id));
//                            _id += valueList.size();
//                        }
//                    }
//                }
//            }
//
//            // Add relationships
//            for (String relationship : relationships.keySet()) {
//                addRelationships(syntaxTree, (Map<String, Object>) relationships.get(relationship));
//            }
//
//            return syntaxTree;
//        } catch (Exception e) {
//            System.out.printf("SyntaxParser.convertToSyntaxTree ERROR: %s%n", e.getMessage());
//            return null;
//        }
//    }
//
//    // Rest of the code remains the same as in the previous version...
//
//    // Update the method signature in addRelationships
//    private void addRelationships(Map<String, Map<String, Object>> syntaxTree, Map<String, Object> relationship) {
//        String source = (String) relationship.get("source");
//        String target = (String) relationship.get("target");
//        Map<String, String> style = (Map<String, String>) relationship.get("style");
//
//        Map<String, Object> sourceCell = syntaxTree.get(source);
//        Map<String, Object> targetCell = syntaxTree.get(target);
//
//        if (style.containsKey("endArrow") && 
//            (style.get("endArrow").toLowerCase().equals("block") || 
//             style.get("endArrow").toLowerCase().equals("none"))) {
//            
//            if (style.get("endArrow").toLowerCase().equals("none") || 
//                style.get("endFill").toLowerCase().equals("1")) {
//                // Association
//                ((List<String>)((Map<String, Object>)targetCell.get("relationships")).get("association")).add(source);
//            } else if (style.containsKey("dashed") && style.get("dashed").equals("1")) {
//                // Implements
//                ((List<String>)((Map<String, Object>)sourceCell.get("relationships")).get("implements")).add(target);
//            } else {
//                // Extends
//                ((List<String>)((Map<String, Object>)sourceCell.get("relationships")).get("extends")).add(target);
//            }
//        } else if ((style.containsKey("endArrow") && style.get("endArrow").toLowerCase().equals("diamondthin")) ||
//                   (style.containsKey("startArrow") && style.get("startArrow").toLowerCase().equals("diamondthin"))) {
//            
//            if (style.containsKey("endFill") && style.get("endFill").equals("1")) {
//                // Composition
//                ((List<String>)((Map<String, Object>)targetCell.get("relationships")).get("composition")).add(source);
//            } else {
//                // Aggregation
//                ((List<String>)((Map<String, Object>)targetCell.get("relationships")).get("aggregation")).add(source);
//            }
//        }
//    }
//
//}	







//
//import java.util.*;
//
//public class SyntaxParser {
//    private Map<String, Object> styleTree;
//    private List<String> classSeries = new ArrayList<>(Arrays.asList("int", "String", "double", "float", "char", "boolean"));
//
//    /**
//     * Constructor for SyntaxParser
//     * 
//     * @param styleTree the style tree of the DrawIO file
//     */
//    public SyntaxParser(Map<String, Object> styleTree) {
//        this.styleTree = styleTree;
//    }
//
//    /**
//     * Convert the style tree to syntax tree
//     * 
//     * @return the syntax tree used by the generators
//     */
//    public Map<String, Map<String, Object>> convertToSyntaxTree() {
//        System.out.println("<<< CONVERTING STYLE TREE TO SYNTAX TREE >>>");
//
//        try {
//            Map<String, Map<String, Object>> syntaxTree = new HashMap<>();
//            Map<String, Object> rootMap = (Map<String, Object>) styleTree.get("root");
//            Map<String, Object> cells = (Map<String, Object>) rootMap.get("cells");
//            Map<String, Object> relationships = (Map<String, Object>) rootMap.get("relationships");
//            String parent = (String) rootMap.get("id");
//
//            boolean propertiesDone = false;
//            int _id = 0;
//
//            for (Map.Entry<String, Object> entry : cells.entrySet()) {
//                String key = entry.getKey();
//                Map<String, Object> value = (Map<String, Object>) entry.getValue();
//
//                // Skip labels for relationships
//                if (relationships.containsKey(value.get("parent_id")) || 
//                    ((Map<String, String>) value.get("style")).containsKey("endArrow")) {
//                    continue;
//                }
//
//                Map<String, String> style = (Map<String, String>) value.get("style");
//                String styleType = style.get("type").toLowerCase();
//
//                if ((value.get("parent_id").equals(parent) && 
//                    (styleType.equals("swimlane") || styleType.equals("html")))) {
//                    // Start of a new cell
//                    syntaxTree.put(key, treeTemplate(value));
//                    propertiesDone = false;
//                    _id = 0;
//                } else {
//                    // Properties and methods in the cell
//                    if (styleType.equals("line") && syntaxTree.containsKey(value.get("parent_id"))) {
//                        propertiesDone = true;
//                        _id = 0;
//                    } else {
//                        Map<String, Object> parentCell = syntaxTree.get(value.get("parent_id"));
//                        List<String> values = (List<String>) value.get("values");
//                        
//                        if (!propertiesDone) {
//                            // Properties
//                            parentCell.get("properties").putAll(propertiesTemplate(value, _id));
//                            _id += values.size();
//                        } else {
//                            // Methods
//                            parentCell.get("methods").putAll(methodsTemplate(value, _id));
//                            _id += values.size();
//                        }
//                    }
//                }
//            }
//
//            // Add relationships
//            for (Map.Entry<String, Object> relationshipEntry : relationships.entrySet()) {
//                Map<String, Object> relationship = (Map<String, Object>) relationshipEntry.getValue();
//                addRelationships(syntaxTree, relationship);
//            }
//
//            return syntaxTree;
//        } catch (Exception e) {
//            System.out.printf("SyntaxParser.convertToSyntaxTree ERROR: %s%n", e.getMessage());
//            return null;
//        }
//    }
//
//    /**
//     * Create the template that will house each cell
//     * 
//     * @param mainCell the starting, parent cell
//     * @return template dictionary
//     */
//    private Map<String, Object> treeTemplate(Map<String, Object> mainCell) {
//        Map<String, Object> template = new HashMap<>();
//        template.put("type", "class");
//        
//        List<String> values = (List<String>) mainCell.get("values");
//        template.put("name", values.isEmpty() ? "" : values.get(0));
//        
//        template.put("properties", new HashMap<>());
//        template.put("methods", new HashMap<>());
//        
//        Map<String, List<String>> relationships = new HashMap<>();
//        relationships.put("implements", new ArrayList<>());
//        relationships.put("extends", new ArrayList<>());
//        relationships.put("association", new ArrayList<>());
//        relationships.put("aggregation", new ArrayList<>());
//        relationships.put("composition", new ArrayList<>());
//        template.put("relationships", relationships);
//
//        Map<String, String> style = (Map<String, String>) mainCell.get("style");
//        
//        if (style.get("type").equals("html")) {
//            int valuesLength = values.size();
//            String name = valuesLength > 0 ? values.get(0) : null;
//            
//            Map<String, Object> properties = new HashMap<>();
//            properties.put("values", valuesLength > 1 ? values.get(1) : null);
//            
//            Map<String, Object> methods = new HashMap<>();
//            methods.put("values", valuesLength > 2 ? values.get(2) : null);
//            
//            template.put("name", name != null ? name.substring(0, 1) : "");
//            template.put("properties", propertiesTemplate(properties, 0));
//            template.put("methods", methodsTemplate(methods, 0));
//        }
//
//        if (style.containsKey("fontStyle") && style.get("fontStyle").equals("2")) {
//            template.put("type", "abstract");
//        } else if (((String) template.get("name")).toLowerCase().startsWith("<<interface>>")) {
//            template.put("type", "interface");
//            template.put("name", ((String) template.get("name")).substring(13));
//        }
//
//        String name = (String) template.get("name");
//        if (!classSeries.contains(name)) {
//            classSeries.add(name);
//        }
//
//        return template;
//    }
//
//    /**
//     * Create the template for properties
//     * 
//     * @param propertyDict the properties dictionary from the style tree
//     * @param _id id for the keys in the dictionary
//     * @return properties template
//     */
//    private Map<Integer, Map<String, String>> propertiesTemplate(Map<String, Object> propertyDict, int _id) {
//        List<String> values = (List<String>) propertyDict.get("values");
//        Map<Integer, Map<String, String>> template = new HashMap<>();
//
//        if (values == null) return template;
//
//        for (String val : values) {
//            if (val.isEmpty()) continue;
//
//            _id++;
//            val = val.strip();
//            char accessModifierSymbol = val.charAt(0);
//            String[] tempVal = val.substring(1).split(":");
//
//            Map<String, String> propertyDetails = new HashMap<>();
//            propertyDetails.put("access", getAccessModifier(accessModifierSymbol));
//            propertyDetails.put("name", tempVal[0].strip());
//            propertyDetails.put("type", tempVal[1].strip());
//
//            template.put(_id, propertyDetails);
//        }
//
//        return template;
//    }
//
//    /**
//     * Create the template for methods
//     * 
//     * @param methodDict the methods dictionary from the style tree
//     * @param _id id for the keys in the dictionary
//     * @return methods template
//     */
//    private Map<Integer, Map<String, String>> methodsTemplate(Map<String, Object> methodDict, int _id) {
//        List<String> values = (List<String>) methodDict.get("values");
//        Map<Integer, Map<String, String>> template = new HashMap<>();
//
//        if (values == null) return template;
//
//        for (String val : values) {
//            if (val.isEmpty()) continue;
//
//            _id++;
//            val = val.strip();
//            char accessModifierSymbol = val.charAt(0);
//            String[] tempVal = val.substring(1).split(":");
//
//            // Handle parameters
//            if (tempVal[0].contains("(")) {
//                int idx = tempVal[0].indexOf('(');
//                String param = tempVal[0].substring(idx + 1, tempVal[0].length() - 1);
//                List<String> paramList = param.isEmpty() ? new ArrayList<>() : Arrays.asList(param.split(","));
//
//                for (int i = 0; i < paramList.size(); i++) {
//                    for (String x : classSeries) {
//                        if (paramList.get(i).strip().startsWith(x)) {
//                            paramList.set(i, paramList.get(i).replace(x, x + " "));
//                        }
//                    }
//                }
//
//                param = String.join(", ", paramList);
//                tempVal[0] = tempVal[0].substring(0, idx + 1) + param + ')';
//            }
//
//            Map<String, String> methodDetails = new HashMap<>();
//            methodDetails.put("access", getAccessModifier(accessModifierSymbol));
//            methodDetails.put("name", tempVal[0].strip());
//            methodDetails.put("return_type", tempVal.length > 1 ? tempVal[1].strip() : "void");
//
//            template.put(_id, methodDetails);
//        }
//
//        return template;
//    }
//
//    /**
//     * Return the access modifier
//     * 
//     * @param symbol symbol representing the access modifier
//     * @return text of the access modifier
//     */
//    private String getAccessModifier(char symbol) {
//        Map<Character, String> accessModifierDict = new HashMap<>();
//        accessModifierDict.put('+', "public");
//        accessModifierDict.put('#', "protected");
//        accessModifierDict.put('-', "private");
//
//        return accessModifierDict.get(symbol);
//    }
//
//    /**
//     * Add the relationship for the cells in the syntax tree
//     * 
//     * @param syntaxTree the syntax tree dictionary
//     * @param relationship relationship to be added to the syntax tree
//     */
//    private void addRelationships(Map<String, Map<String, Object>> syntaxTree, Map<String, Object> relationship) {
//        String source = (String) relationship.get("source");
//        String target = (String) relationship.get("target");
//        Map<String, String> style = (Map<String, String>) relationship.get("style");
//
//        Map<String, Object> sourceCell = syntaxTree.get(source);
//        Map<String, Object> targetCell = syntaxTree.get(target);
//
//        if (style.containsKey("endArrow") && 
//            (style.get("endArrow").toLowerCase().equals("block") || 
//             style.get("endArrow").toLowerCase().equals("none"))) {
//            
//            if (style.get("endArrow").toLowerCase().equals("none") || 
//                style.get("endFill").toLowerCase().equals("1")) {
//                // association
//                ((List<String>) targetCell.get("relationships.association")).add(source);
//            } else if (style.containsKey("dashed") && style.get("dashed").equals("1")) {
//                // implements
//                ((List<String>) sourceCell.get("relationships.implements")).add(target);
//            } else {
//                // extends
//                ((List<String>) sourceCell.get("relationships.extends")).add(target);
//            }
//        } else if ((style.containsKey("endArrow") && 
//                    style.get("endArrow").toLowerCase().equals("diamondthin")) || 
//                   (style.containsKey("startArrow") && 
//                    style.get("startArrow").toLowerCase().equals("diamondthin"))) {
//            
//            if (style.containsKey("endFill") && style.get("endFill").equals("1")) {
//                // composition
//                ((List<String>) targetCell.get("relationships.composition")).add(source);
//            } else {
//                // aggregation
//                ((List<String>) targetCell.get("relationships.aggregation")).add(source);
//            }
//        }
//    }
//
//    // Optional main method for testing
//    public static void main(String[] args) {
//        // Example usage
//        Map<String, Object> sampleStyleTree = new HashMap<>();
//        // Populate sampleStyleTree with your DrawIO XML-derived data
//        SyntaxParser parser = new SyntaxParser(sampleStyleTree);
//        Map<String, Map<String, Object>> syntaxTree = parser.convertToSyntaxTree();
//        
//        if (syntaxTree != null) {
//            System.out.println("Syntax Tree successfully created.");
//            // Add additional print or processing logic here
//        }
//    }
//}