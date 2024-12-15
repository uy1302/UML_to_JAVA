package Generator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JavaCodeGenerator {
	private Map<String, Map<String, Object>> syntaxTree;
	private String filePath;
    private List<Object> classes;
    private List<Object> properties;
    private List<Object> methods;
    private List<List<String>> files;
    
    public JavaCodeGenerator (Map<String, Map<String, Object>> syntaxTree) {
    	this.syntaxTree = syntaxTree;
    	this.classes = new ArrayList<>();
    	this.properties = new ArrayList<>();
    	this.methods = new ArrayList<>();
    	this.files = new ArrayList<>();
    }

    //1
    public void generateCode() {
        /*
         * Use the syntax tree to generate code files for the UML class diagrams 
         */
        
        System.out.println("<<< GENERATING CODE FILES FROM SYNTAX TREE >>>");

        try {
            for (Map.Entry<String, Map<String, Object>> entry : this.syntaxTree.entrySet()) {
                String file = "";
                Map<String, Object> _class = entry.getValue();
                
                String inheritance = "";
                
                List<String> extendsRelationships = (List<String>) ((Map<String, Object>) _class.get("relationships")).get("extends");
                if (!extendsRelationships.isEmpty()) {
                    StringBuilder inheritanceBuilder = new StringBuilder("extends ");
                    boolean first = true;
                    for (String r : extendsRelationships) {
                        if (!first) {
                            inheritanceBuilder.append(",");
                        }
                        inheritanceBuilder.append(this.syntaxTree.get(r).get("name"));
                        first = false;
                    }
                    inheritance = inheritanceBuilder.toString().strip();
                }
                
                String implementation = "";
                List<String> implementsRelationships = (List<String>) ((Map<String, Object>) _class.get("relationships")).get("implements");
                if (!implementsRelationships.isEmpty()) {
                    StringBuilder implementationBuilder = new StringBuilder("implements ");
                    boolean first = true;
                    for (String r : implementsRelationships) {
                        if (!first) {
                            implementationBuilder.append(",");
                        }
                        implementationBuilder.append(this.syntaxTree.get(r).get("name"));
                        first = false;
                    }
                    implementation = implementationBuilder.toString().strip();
                }

                List<Map<String, String>> interfaceMethods = new ArrayList<>();
                getInterfaceMethods(implementsRelationships, interfaceMethods);
                file += generateClasses((String) _class.get("type"), (String) _class.get("name"), inheritance, implementation);
                file += "\n";
                file += generateProperties((Map<String, Map<String, String>>) _class.get("properties"));
                file += "\n";
//                System.out.println(file);
                file += generateMethods(
                    (Map<String, Map<String, String>>) _class.get("methods"), 
                    (Map<String, Map<String, String>>) _class.get("properties"), 
                    (String) _class.get("type"), 
                    interfaceMethods
                );
                
                file += "}\n";
                
                List<String> check_lst = Arrays.asList((String) _class.get("name"), file);
                this.files.add(check_lst);
//                System.out.println(file);
            }

//            generateFiles();
        }
        catch (Exception e) {
            System.err.println("JavaCodeGenerator.generateCode ERROR: " + e.getMessage());
        }
    }
    
    
    //2
    public String generateProperties(Map<String, Map<String, String>> properties) {
        String propertiesString = "";

        for (Map.Entry<String, Map<String, String>> entry : properties.entrySet()) {
            Map<String, String> propertyValue = entry.getValue();
            String access = propertyValue.get("access");
            String type = propertyValue.get("type");
            String name = propertyValue.get("name");

            String propertyString = "\t" + access + " " + type + " " + name + ";\n";
            this.properties.add(propertyString);
            propertiesString += propertyString;
        }
        return propertiesString;
    }
    
    
    //3
    private String generateClasses(String classType, String className, String extendsStr, String implementsStr) {
        String typeOfClass = classType.equals("class") ? "public class" : classType;
        typeOfClass = classType.equals("abstract") ? "abstract class" : typeOfClass;
        String classHeader = typeOfClass + " " + className + " " + extendsStr + " " + implementsStr + " {\n";
        classHeader = classHeader.replaceAll(" +", " ");
        classes.add(classHeader);
        return classHeader;
    }
    
    //4
    public String generateMethods(
            Map<String, Map<String, String>> methods, 
            Map<String, Map<String, String>> properties, 
            String classType, 
            List<Map<String, String>> interfaceMethods
        ) {
            StringBuilder methodsString = new StringBuilder();
            List<String> privateMethodsList = new ArrayList<>();

            // Generate methods from the methods map
            for (Map<String, String> methodMap : methods.values()) {
                String m;
                if (methodMap.get("name").contains("(")) {
                    m = String.format("\t%s %s %s {}\n", 
                        methodMap.get("access"), 
                        methodMap.get("return_type"), 
                        methodMap.get("name")
                    );
                } else {
                    m = String.format("\t%s %s %s() {}\n", 
                        methodMap.get("access"), 
                        methodMap.get("return_type"), 
                        methodMap.get("name")
                    );
                }

                methodsString.append(m).append("\n");
                privateMethodsList.add(m);
            }

            // Generate getters and setters for private properties
            if ("class".equals(classType) || "abstract".equals(classType)) {
//                for (Map<String, String> propertyValue : properties.values()) {
//                    if ("private".equals(propertyValue.get("access"))) {
//                        String propertyName = propertyValue.get("name");
//                        String propertyType = propertyValue.get("type");
//                        String capitalizedName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
//
//                        // Getter
//                        String getter = String.format(
//                            "\tpublic %s get%s() {\n\t\treturn this.%s;\n\t}\n", 
//                            propertyType, capitalizedName, propertyName
//                        );
//                        methodsString.append(getter).append("\n");
//                        privateMethodsList.add(getter);
//
//                        // Setter
//                        String setter = String.format(
//                            "\tpublic void set%s(%s %s) {\n\t\tthis.%s = %s;\n\t}\n", 
//                            capitalizedName, propertyType, propertyName, propertyName, propertyName
//                        );
//                        methodsString.append(setter).append("\n");
//                        privateMethodsList.add(setter);
//                    }
//                }

                // Generate interface method stubs
                for (Map<String, String> interfaceMethod : interfaceMethods) {
                    String comment = "// ***requires implementation***";
                    String m = String.format(
                        "\t%s %s %s() {\n\t\t%s\n\t}\n", 
                        interfaceMethod.get("access"), 
                        interfaceMethod.get("return_type"), 
                        interfaceMethod.get("name"), 
                        comment
                    );
                    methodsString.append(m).append("\n");
                    privateMethodsList.add(m);
                }
            }

            return methodsString.toString();
        }
    
    
    //5
    public void getInterfaceMethods(List<String> implementsList, List<Map<String, String>> interfaceMethods) {
        for (String i : implementsList) {
            // Assuming syntaxTree is a Map<String, Object>
            Map<String, Object> interfaceObj = (Map<String, Object>) syntaxTree.get(i);

            // Assuming "methods" is a Map<String, String> inside the interface object
            Map<String, String> methods = (Map<String, String>) interfaceObj.get("methods");
            interfaceMethods.add((Map<String, String>) methods.values());

            // Recursively call for implemented interfaces
            List<String> subImplements = (List<String>) ((Map<String, Object>) interfaceObj.get("relationships")).get("implements");
            getInterfaceMethods(subImplements, interfaceMethods);
        }
    }
    
    
    public String generateClassStructure(Map<String, Map<String, Object>> syntaxTree) {
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, Map<String, Object>> entry : syntaxTree.entrySet()) {
            Map<String, Object> _class = entry.getValue();
            String className = (String) _class.get("name");
            String classType = (String) _class.get("type");

            Map<String, List<String>> relationships = (Map<String, List<String>>) _class.get("relationships");
            List<String> extendsList = relationships.get("extends");
            List<String> implementsList = relationships.get("implements");

            StringBuilder inheritanceBuilder = new StringBuilder();
            if (extendsList != null && !extendsList.isEmpty()) {
                String parentClassId = extendsList.get(0); 
                String parentClassName = (String) syntaxTree.get(parentClassId).get("name");
                inheritanceBuilder.append(" extends ").append(parentClassName);
            }
            if (implementsList != null && !implementsList.isEmpty()) {
                List<String> interfaceNames = implementsList.stream()
                        .map(id -> (String) syntaxTree.get(id).get("name"))
                        .collect(Collectors.toList());
                inheritanceBuilder.append(" implements ").append(String.join(", ", interfaceNames));
            }

            builder.append("    \"public ").append(classType).append(" ").append(className)
                    .append(inheritanceBuilder).append("\": {\n");

            // Add attributes
            Map<String, Map<String, String>> properties = (Map<String, Map<String, String>>) _class.get("properties");
            builder.append("        \"attributes\": {\n");
            for (Map.Entry<String, Map<String, String>> propEntry : properties.entrySet()) {
                Map<String, String> propDetails = propEntry.getValue();
                String access = propDetails.get("access");
                String type = propDetails.get("type");
                String name = propDetails.get("name");
                builder.append("            \"").append(name).append("\": \"").append(access).append(" ").append(type)
                        .append(" ").append(name).append("\",\n");
            }
            if (!properties.isEmpty()) {
                builder.setLength(builder.length() - 2);
            }
            builder.append("\n        },\n");

            // Add methods
            Map<String, Map<String, String>> methods = (Map<String, Map<String, String>>) _class.get("methods");
            builder.append("        \"methods\": {\n");
            for (Map.Entry<String, Map<String, String>> methodEntry : methods.entrySet()) {
                Map<String, String> methodDetails = methodEntry.getValue();
                String access = methodDetails.get("access");
                String returnType = methodDetails.get("return_type");
                String methodName = methodDetails.get("name");
                builder.append("            \"").append(methodName).append("\": \"").append(access).append(" ")
                        .append(returnType).append(" ").append(methodName).append("\",\n");
            }
            if (!methods.isEmpty()) {
                builder.setLength(builder.length() - 2); 
            }
            builder.append("\n        }\n    },\n");
        }
        builder.setLength(builder.length() - 2); 
        builder.append("\n}");

//        System.out.println(builder.toString());
        return "{"+builder.toString();
    }

    
    public String generateDescription(Map<String, Map<String, Object>> syntaxTree) {
        StringBuilder formattedDescription = new StringBuilder();
        for (Map.Entry<String, Map<String, Object>> entry : syntaxTree.entrySet()) {
            Map<String, Object> _class = entry.getValue();
            String className = (String) _class.get("name");
            String classType = (String) _class.get("type");

            Map<String, List<String>> relationships = (Map<String, List<String>>) _class.get("relationships");
            List<String> extendsList = relationships.get("extends");
            List<String> implementsList = relationships.get("implements");

            StringBuilder inheritanceBuilder = new StringBuilder();
            if (extendsList != null && !extendsList.isEmpty()) {
                String parentClassId = extendsList.get(0); 
                String parentClassName = (String) syntaxTree.get(parentClassId).get("name");
                inheritanceBuilder.append(" extends ").append(parentClassName);
            }
            if (implementsList != null && !implementsList.isEmpty()) {
                List<String> interfaceNames = implementsList.stream()
                        .map(id -> (String) syntaxTree.get(id).get("name"))
                        .collect(Collectors.toList());
                inheritanceBuilder.append(" implements ").append(String.join(", ", interfaceNames));
            }

            // Add class 
            formattedDescription.append("public class ").append(className)
                    .append(inheritanceBuilder).append(":\n");

            // Add methods
            Map<String, Map<String, String>> methods = (Map<String, Map<String, String>>) _class.get("methods");
            for (Map.Entry<String, Map<String, String>> methodEntry : methods.entrySet()) {
                Map<String, String> methodDetails = methodEntry.getValue();
                String methodName = methodDetails.get("name");
                String methodNameBeforeParen = methodName.split("\\(")[0].trim();
                formattedDescription.append("    ").append(methodNameBeforeParen).append(":\n");
            }
        }

        // Output the final formatted description
        System.out.println(formattedDescription.toString());
        return formattedDescription.toString();
    }




    public void generateFiles() {
    	System.out.println("<<< WRITING FILES TO " + filePath + " >>>");
    	
    	try {
    		for (List<String> file : files) {
    			String fileName = file.get(0) + ".java";
    			String fileContents = file.get(1);
    			
    			File outputFile = new File(filePath + File.separator + fileName);
    			
    			try(FileWriter writer = new FileWriter(outputFile)) {
    				writer.write(fileContents);
    			}
    		}
    	} catch (Exception e) {
    		System.err.println("JavaCodeGenerator.generateFiles ERROR: " + e.getMessage());
    	}
    	
    }


    //7
    private List<Object> GetClasses() {
    	return classes;
    }
    
    //8
    private List<Object> GetProperties() {
    	return properties;
    }
    
    //9
    private List<Object> GetMethods() {
    	return methods;
    }
    //10
    private List<List<String>> GetFiles() {
    	return files;
    }
    
}