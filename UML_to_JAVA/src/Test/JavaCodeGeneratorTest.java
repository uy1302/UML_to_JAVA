package Test;
import java.util.Map;

import Decode.DecodeAndCompress;
import Generator.JavaCodeGenerator;
import Parser.StyleParser;
import Parser.SyntaxParser;

public class JavaCodeGeneratorTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        String drawioFilepath = "/Users/phamkien/Downloads/AIMS (4).drawio";
        System.out.println(drawioFilepath);
        String decoded_xml = DecodeAndCompress.convert(drawioFilepath);
		StyleParser parser_style = new StyleParser(decoded_xml);
		System.out.println(decoded_xml);
//		System.out.println(parser_style.convertToStyleTree());
		Map<String, Object> style_tree = parser_style.convertToStyleTree();
		System.out.println(style_tree);
		
		
		SyntaxParser parser_syntax = new SyntaxParser(style_tree);
		Map<String, Map<String, Object>> syntax_tree = parser_syntax.convertToSyntaxTree();
		System.out.println(syntax_tree);
		JavaCodeGenerator java_gen = new JavaCodeGenerator(syntax_tree);
		java_gen.generateCode();
//		java_gen.generateClassStructure();
//		java_gen.generateDescription();
	}
}
