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
        	addClassList();
        	
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

        return template;
    }
    
    private void addClassList() {
        Map<String, Object> cells = (Map<String, Object>) ((Map<String, Object>) styleTree.get("root")).get("cells");

        if (cells == null) {
            System.out.println("No cells found in the style tree.");
            return;
        }

        for (Map.Entry<String, Object> entry : cells.entrySet()) {
            Map<String, Object> cell = safeCastToMap(entry.getValue());
            if (cell == null) continue;

            Map<String, Object> style = safeCastToMap(cell.get("style"));
            if (style == null) continue;

            // Check if the cell represents a class-like element
            String styleType = safeString(style.get("type")).toLowerCase();
            if (styleType.equals("swimlane") || styleType.equals("html")) {
                List<String> values = safeCastToList(cell.get("values"));
                if (values == null || values.isEmpty()) continue;

//                String className = values.get(0).strip();
                String className = values.get(0).trim();
                if (!classSLst.contains(className)) {
                    classSLst.add(className);
                }
            }
        }
        System.out.println("Class names added to classSLst: " + classSLst);
    }

    private Map<String, Object> safeCastToMap(Object obj) {
        return (obj instanceof Map) ? (Map<String, Object>) obj : null;
    }

    private List<String> safeCastToList(Object obj) {
        return (obj instanceof List) ? (List<String>) obj : null;
    }

    private String safeString(Object obj) {
        return (obj instanceof String) ? (String) obj : "";
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
//            val = val.strip();
            val = val.trim();
            char accessModifierSymbol = val.charAt(0);
            String[] tempVal = val.substring(1).split(":");

            Map<String, Object> propertyDetails = new HashMap<>();
            propertyDetails.put("access", getAccessModifier(accessModifierSymbol));
//            propertyDetails.put("name", tempVal[0].strip());
//            propertyDetails.put("type", tempVal[1].strip());
            propertyDetails.put("name", tempVal[0].trim());
            propertyDetails.put("type", tempVal[1].trim());
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
//            val = val.strip();
            val = val.trim();
            char accessModifierSymbol = val.charAt(0);
            String[] tempVal = val.substring(1).split(":");

            // Handle parameters
            if (tempVal[0].contains("(")) {
                int idx = tempVal[0].indexOf('(');
                String param = tempVal[0].substring(idx + 1, tempVal[0].length() - 1);
                List<String> paramList = param.isEmpty() ? new ArrayList<>() : Arrays.asList(param.split(","));

                for (int i = 0; i < paramList.size(); i++) {
                    for (String x : classSLst) {
//                        if (paramList.get(i).strip().startsWith(x)) {
                    	if (paramList.get(i).trim().startsWith(x)) {
                            paramList.set(i, paramList.get(i).replace(x, x + " "));
                        }
                    }
                }

                param = String.join(", ", paramList);
                tempVal[0] = tempVal[0].substring(0, idx + 1) + param + ')';
            }

            Map<String, Object> methodDetails = new HashMap<>();
            methodDetails.put("access", getAccessModifier(accessModifierSymbol));
//            methodDetails.put("name", tempVal[0].strip());
            methodDetails.put("name", tempVal[0].trim());
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
