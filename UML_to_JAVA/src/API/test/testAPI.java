package API.test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import API.utils.connectAPI;
import API.utils.jsonConverter;
//import exceptions.descriptionException;

import java.io.*;

public class testAPI {
	 public static void main(String[] args) {
			String apiUrl = "http://127.0.0.1:8000";
			 String descriptions = "public class Book extends Media\r\n"
			 		+ "   Book(String title): constructor\r\n"
			 		+ "   Book(String title, String category, float cost, List<String>authors): constructor\r\n"
			 		+ "   getAuthors():get all authors' names\r\n"
			 		+ "   Book(String title, String category, float cost): constructor\r\n"
			 		+ "   addAuthor(String authorName):add an author\r\n"
			 		+ "   removeAuthor(String authorName):remove an author\r\n"
			 		+ "   toString(): get all information\r\n"
			 		+ "public class Track implements Playable\r\n"
			 		+ "   Track(String title, int length): constructor\r\n"
			 		+ "   getTitle():get title\r\n"
			 		+ "   equals(Track track):compare two tracks\r\n"
			 		+ "   play():print out title and length\r\n"
			 		+ "interface Playable\r\n"
			 		+ "   play():interface\r\n"
			 		+ "public class Disc extends Media\r\n"
			 		+ "   Disc(String title): constructor\r\n"
			 		+ "   Disc(String title, String category, float cost): constructor\r\n"
			 		+ "   Disc(String title, String category, String director, float cost): constructor\r\n"
			 		+ "   getLength():get length\r\n"
			 		+ "   getDirector():get director\r\n"
			 		+ "public class DigitalVideoDisc extends Disc implements Playable\r\n"
			 		+ "   DigitalVideoDisc(String title): constructor\r\n"
			 		+ "   DigitalVideoDisc(String title, String category, float cost): constructor\r\n"
			 		+ "   DigitalVideoDisc(String title, String category, String director, float cost):constructor\r\n"
			 		+ "   DigitalVideoDisc(String title, String category, String director, int length, float cost):constructor\r\n"
			 		+ "   toString():get all information\r\n"
			 		+ "   play():get length and title\r\n"
			 		+ "abstract class Media\r\n"
			 		+ "   Media(String title):constructor\r\n"
			 		+ "   Media(String title, String category, float cost):constructor\r\n"
			 		+ "   getId():get Id\r\n"
			 		+ "   getTitle():get title\r\n"
			 		+ "   getCategory():get category\r\n"
			 		+ "   getCost():get cost\r\n"
			 		+ "   equals(Media other):compare two Media\r\n"
			 		+ "public class CompactDisc extends Disc implements Playable\r\n"
			 		+ "   getArtist():get artist\r\n"
			 		+ "   CompactDisc(String title, String Category, String director, int length, float cost, String artist, List<Track>tracks):constructor\r\n"
			 		+ "   CompactDisc(String title, String Category, String director, int length, float cost):constructor\r\n"
			 		+ "   CompactDisc(String title):constructor\r\n"
			 		+ "   getTracks():get all tracks\r\n"
			 		+ "   play():print out title and length\r\n"
			 		+ "   toString():get all information\r\n"
			 		+ "   removeTrack(Track track):remove a track having the same title\r\n"
			 		+ "   addTrack(Track track):add a track if there is no track with the same title\r\n"
			 		+ "";
			descriptions = jsonConverter.StringtoJson(descriptions);
			String classes = "{    \"public class Book extends Media\": {\r\n"
					+ "        \"attributes\": {\r\n"
					+ "            \"authors\": \"private List<String> authors \"\r\n"
					+ "        },\r\n"
					+ "        \"methods\": {\r\n"
					+ "            \"Book(String title)\": \"public  Book(String title)\",\r\n"
					+ "            \"Book(String title, String category, float cost, List<String>authors)\": \"public  Book(String title, String category, float cost, List<String>authors)\",\r\n"
					+ "            \"getAuthors()\": \"public List<String> getAuthors()\",\r\n"
					+ "            \"Book(String title, String category, float cost)\": \"public  Book(String title, String category, float cost)\",\r\n"
					+ "            \"addAuthor(String authorName)\": \"public void addAuthor(String authorName)\",\r\n"
					+ "            \"removeAuthor(String authorName)\": \"public void removeAuthor(String authorName)\",\r\n"
					+ "            \"toString()\": \"public String toString()\"\r\n"
					+ "        }\r\n"
					+ "    },\r\n"
					+ "    \"public class Track implements Playable\": {\r\n"
					+ "        \"attributes\": {\r\n"
					+ "            \"title\": \"private String title \",\r\n"
					+ "            \"length\": \"private int length \"\r\n"
					+ "        },\r\n"
					+ "        \"methods\": {\r\n"
					+ "            \"Track(String title, int length)\": \"public  Track(String title, int length)\",\r\n"
					+ "            \"getTitle()\": \"public String getTitle()\",\r\n"
					+ "            \"equals(Track track)\": \"public boolean equals(Track track)\",\r\n"
					+ "            \"play()\": \"public void play()\"\r\n"
					+ "        }\r\n"
					+ "    },\r\n"
					+ "    \"interface Playable\": {\r\n"
					+ "        \"attributes\": {\r\n"
					+ "\r\n"
					+ "        },\r\n"
					+ "        \"methods\": {\r\n"
					+ "            \"play()\": \"public void play()\"\r\n"
					+ "        }\r\n"
					+ "    },\r\n"
					+ "    \"public class Disc extends Media\": {\r\n"
					+ "        \"attributes\": {\r\n"
					+ "            \"length\": \"private int length \",\r\n"
					+ "            \"director\": \"private String director \"\r\n"
					+ "        },\r\n"
					+ "        \"methods\": {\r\n"
					+ "            \"Disc(String title)\": \"public  Disc(String title)\",\r\n"
					+ "            \"Disc(String title, String category, float cost)\": \"public  Disc(String title, String category, float cost)\",\r\n"
					+ "            \"Disc(String title, String category, String director, float cost)\": \"public  Disc(String title, String category, String director, float cost)\",\r\n"
					+ "            \"getLength()\": \"public int getLength()\",\r\n"
					+ "            \"getDirector()\": \"public String getDirector()\"\r\n"
					+ "        }\r\n"
					+ "    },\r\n"
					+ "    \"public class DigitalVideoDisc extends Disc implements Playable\": {\r\n"
					+ "        \"attributes\": {\r\n"
					+ "\r\n"
					+ "        },\r\n"
					+ "        \"methods\": {\r\n"
					+ "            \"DigitalVideoDisc(String title)\": \"public  DigitalVideoDisc(String title)\",\r\n"
					+ "            \"DigitalVideoDisc(String title, String category, float cost)\": \"public  DigitalVideoDisc(String title, String category, float cost)\",\r\n"
					+ "            \"DigitalVideoDisc(String title, String category, String director, float cost)\": \"public  DigitalVideoDisc(String title, String category, String director, float cost)\",\r\n"
					+ "            \"DigitalVideoDisc(String title, String category, String director, int length, float cost)\": \"public  DigitalVideoDisc(String title, String category, String director, int length, float cost)\",\r\n"
					+ "            \"toString()\": \"public String toString()\",\r\n"
					+ "            \"play()\": \"public void play()\"\r\n"
					+ "        }\r\n"
					+ "    },\r\n"
					+ "    \"abstract class Media\": {\r\n"
					+ "        \"attributes\": {\r\n"
					+ "            \"id\": \"private int id \",\r\n"
					+ "            \"title\": \"private String title \",\r\n"
					+ "            \"category\": \"private String category \",\r\n"
					+ "            \"cost\": \"private float cost \",\r\n"
					+ "            \"nbMedia\": \"private int nbMedia \"\r\n"
					+ "        },\r\n"
					+ "        \"methods\": {\r\n"
					+ "            \"Media(String title)\": \"public  Media(String title)\",\r\n"
					+ "            \"Media(String title, String category, float cost)\": \"public  Media(String title, String category, float cost)\",\r\n"
					+ "            \"getId()\": \"public int getId()\",\r\n"
					+ "            \"getTitle()\": \"public String getTitle()\",\r\n"
					+ "            \"getCategory()\": \"public String getCategory()\",\r\n"
					+ "            \"getCost()\": \"public float getCost()\",\r\n"
					+ "            \"equals(Media other)\": \"public boolean equals(Media other)\"\r\n"
					+ "        }\r\n"
					+ "    },\r\n"
					+ "    \"public class CompactDisc extends Disc implements Playable\": {\r\n"
					+ "        \"attributes\": {\r\n"
					+ "            \"artist\": \"private String artist \",\r\n"
					+ "            \"track\": \"private List<Track> track \"\r\n"
					+ "        },\r\n"
					+ "        \"methods\": {\r\n"
					+ "            \"getArtist()\": \"public String getArtist()\",\r\n"
					+ "            \"CompactDisc(String title, String Category, String director, int length, float cost, String artist, List<Track>tracks)\": \"public  CompactDisc(String title, String Category, String director, int length, float cost, String artist, List<Track>tracks)\",\r\n"
					+ "            \"CompactDisc(String title, String Category, String director, int length, float cost)\": \"public  CompactDisc(String title, String Category, String director, int length, float cost)\",\r\n"
					+ "            \"CompactDisc(String title)\": \"public  CompactDisc(String title)\",\r\n"
					+ "            \"getTracks()\": \"public List<Track> getTracks()\",\r\n"
					+ "            \"play()\": \"public void play()\",\r\n"
					+ "            \"toString()\": \"public String toString()\",\r\n"
					+ "            \"removeTrack(Track track)\": \"public void removeTrack(Track track)\",\r\n"
					+ "            \"addTrack(Track track)\": \"public void addTrack(Track track)\"\r\n"
					+ "        }\r\n"
					+ "    }\r\n"
					+ "}";
			System.out.println(descriptions);
			System.out.println(classes);
			int postResponseCode = connectAPI.postAPI(descriptions, classes);
			System.out.println("Response Code: " + postResponseCode);
			if (postResponseCode == HttpURLConnection.HTTP_OK) {
				connectAPI.runPython("test.py");
				Map<String, String> javaCode = connectAPI.getCode();
				for (Map.Entry<String, String> entry : javaCode.entrySet()) {
                    System.out.println("Key: " + entry.getKey());
                    System.out.println("Value: " + entry.getValue().replace("\\t", "\t"));
                    System.out.println();
                }
			}else {
				System.out.println("ERROR! Wrong format description!");
			}
	}
}

