package Decode;
import java.nio.file.*;
import java.util.zip.Inflater;
import java.util.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.net.URLDecoder;
import java.io.*;
public class DecodeAndCompress {
    public static String convert(String drawioFilePath) {
        try {
            // Đọc nội dung file
            String content = new String(Files.readAllBytes(Paths.get(drawioFilePath)));
//            System.out.println(content);
            // Parse nội dung để lấy thẻ <diagram>
            Document document = Jsoup.parse(content);
//            System.out.println(document);
            Element diagramTag = document.selectFirst("diagram");
            if (diagramTag == null) {
                System.err.println("Không tìm thấy thẻ <diagram> trong file.");
                return null;
            }

            // Giải mã Base64
            byte[] decodedBase64 = Base64.getDecoder().decode(diagramTag.text());

            // Giải nén với zlib
            Inflater inflater = new Inflater(true);
            inflater.setInput(decodedBase64);
            byte[] buffer = new byte[8192];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            inflater.end();

            // Decode UTF-8 và unquote
            String decompressedXml = new String(outputStream.toByteArray(), "UTF-8");
            return URLDecoder.decode(decompressedXml, "UTF-8");
        } catch (Exception e) {
            System.err.println("DecodeAndCompress.convert ERROR: " + e.getMessage());
            return null;
        }
    }

    public static void write_xml_file(String xml_fileName, String decoded_xml) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(xml_fileName + ".xml"))) {
            writer.write(decoded_xml);
            System.out.println("XML đã được ghi vào file: " + xml_fileName + ".xml");
        } catch (IOException e) {
            System.err.println("DecodeAndCompress.write_xml_file ERROR: " + e.getMessage());
        }
    }
}
