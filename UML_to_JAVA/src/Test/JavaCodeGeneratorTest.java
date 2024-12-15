package Test;
import java.util.Map;

import Decode.DecodeAndCompress;
import Generator.JavaCodeGenerator;
import Parser.StyleParser;
import Parser.SyntaxParser;

public class JavaCodeGeneratorTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        String drawioFilepath = "C:/Users/DELL INSPIRON/OneDrive/Documents/UML_to_JAVA/UML_to_JAVA/Test5.drawio";
        String decoded_xml = DecodeAndCompress.convert(drawioFilepath);
		StyleParser parser_style = new StyleParser(decoded_xml);
//		System.out.println(decoded_xml);
//		System.out.println(parser_style.convertToStyleTree());
		Map<String, Object> style_tree = parser_style.convertToStyleTree();
//		System.out.println(style_tree);
		
		
		SyntaxParser parser_syntax = new SyntaxParser(style_tree);
		Map<String, Map<String, Object>> syntax_tree = parser_syntax.convertToSyntaxTree();

		JavaCodeGenerator java_gen = new JavaCodeGenerator(syntax_tree);
		java_gen.generateCode();
		java_gen.generateClassStructure(syntax_tree);
		java_gen.generateDescription(syntax_tree);
	}
}

