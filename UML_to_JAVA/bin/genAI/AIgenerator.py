import google.generativeai as genai
import os
import requests

class AIagent():

	def __init__(self, api_key):
		genai.configure(api_key=api_key)
		self.model = genai.GenerativeModel("gemini-1.5-flash")

	def generateCodeByDescriptions(self, descriptions, classes):
		prompt = """You are a senior software developer. Complete the following methods of classes based on the discriptions, 
		without changing the class name or method signatures. Only need to write the method body, without any explanations: \n"""
		for class_ in classes:
			prompt += class_ + " {\n"
			for attribute in classes[class_]['attributes']:
				prompt += "\t" + classes[class_]['attributes'][attribute] + ";\n"
			prompt += "\n"
			for method in classes[class_]['methods']:
				prompt += "\t" + classes[class_]['methods'][method] + "\t//" + descriptions[class_][method] + "\n"  
			
			prompt += "}\n"
		
		print(prompt)	
		response = self.model.generate_content(prompt)
		return self.postProcessResponses(response)

	def postProcessResponses(self, response):
		return_text = response.text.replace("```","").split("\n")[1:-1]
		return_file = []
		i = 0
		while i < len(return_text):
			if return_text[i].find("class") != -1:
				file = [return_text[i].split()[return_text[i].split().index("class") + 1]]
				content = return_text[i] + "\n"
				i += 1
				while i < len(return_text) and return_text[i].find("class") == -1:
					content += return_text[i] + "\n"
					i += 1
				file.append(content)
				return_file.append(file)
			else:
				i += 1
		return return_file 

	def generateCodeByClasses(self, prompt):

		prefix = """You are a senior software developer. Complete the following methods of classes, 
		without changing the class name or method signatures. Only need to write the method body, without any explanations: \n"""
		prompt = prefix + prompt
		response = self.model.generate_content(prompt)
		print(response.text)
		return self.postProcessResponses(response)


if __name__ == "__main__":
	agent = AIagent(input("Enter your API key: "))
	if agent:
		# choice = int(input(""""Choose an option:
		# 			 1. Generate code by description
		# 	2. Exit
		# 	"""))
		# prompt = input("Enter your prompt: ")
		# prompt = """You are a senior software developer. Complete the method based on the following description:
		# You are given an integer n and a 2D integer array queries.

		# There are n cities numbered from 0 to n - 1. Initially, there is a unidirectional road from city i to city i + 1 for all 0 <= i < n - 1.

		# queries[i] = [ui, vi] represents the addition of a new unidirectional road from city ui to city vi. After each query, you need to find the length of the shortest path from city 0 to city n - 1.

		# Return an array answer where for each i in the range [0, queries.length - 1], answer[i] is the length of the shortest path from city 0 to city n - 1 after processing the first i + 1 queries.
		# The response should start from class Solution and not containing anything else rather than source code
		# class Solution {
		# 	public int[] shortestDistanceAfterQueries(int n, int[][] queries) {

		# 	}
		# }"""
		# agent.generateCodeByDescriptions(prompt)
		user_input = """import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hust.soict.dsai.aims.media.Media;

public class Cart {
	private List<Media> itemsOrdered = new ArrayList<Media>(); 
	
	public void addMedia(Media item) {
	}
	
	public void removeMedia(Media item) {
	}
	
	public float totalCost() {
	}
	
	public void getAllTitles() {
	}
	
	public void print() {
	}
	
	public void search(int id) {
	}
	
	public void search(String title) {
	}
	
	public void sortByTitleCost() {
	}
	
	public void sortByCostTitle() {
	}
	
	public int getNumberOfMedia() {
	}
	
	public void empty() {
	}
}"""

		res = agent.generateCodeByClasses(user_input)
		for file in res:
			file_name = file[0] + ".java"
			# print(file_name)
			file_contents = file[1]
			# print(file_contents)
			write_path = "Convert-UML-to-Java-code/examples/example_code" + f"/{file_name}"
			if os.path.exists(write_path):
				with open(write_path, "w") as f:
					f.write(file_contents)
			else:
				os.makedirs(os.path.dirname(write_path), exist_ok=True)
				with open(write_path, "x") as f:
					f.write(file_contents)
		descriptions = {
			"public class Vehicle": {
				"start": "The Vehicle initializes its engine and prepares all systems for operation.",
				"stop": "The Vehicle powers down its engine and deactivates non-essential systems.",
				"accelerate": "The Vehicle increases its speed by applying more power to its drivetrain.",
				"brake": "The Vehicle slows down or comes to a stop by applying its braking mechanism."
			},
			"public class Car": {
				"playMusic": "The Car's entertainment system plays music for the passengers.",
				"enableCruiseControl": "The Car maintains a steady speed without driver input through its cruise control system."
			},
			"public class ElectricCar": {
				"chargeBattery": "The ElectricCar connects to a charging station to replenish its battery.",
				"regeneratePower": "The ElectricCar converts kinetic energy into electrical energy during braking to recharge the battery."
			}
		}


		classes = {
			"public class Vehicle": {
				"attributes": {
					"make": "private String make",
					"model": "private String model",
					"year": "private int year",
					"speed": "private double speed"
				},
				"methods": {
					"start": "void start()",
					"stop": "void stop()",
					"accelerate": "void accelerate(doubleincrement)",
					"brake": "void brake(doubledecrement)"
				}
			},
			"public class Car": {
				"attributes": {
					"entertainmentSystem": "private String entertainmentSystem",
					"seatingCapacity": "private int seatingCapacity"
				},
				"methods": {
					"playMusic": "void playMusic(StringsongName)",
					"enableCruiseControl": "void enableCruiseControl(doublespeed)"
				}
			},
			"public class ElectricCar": {
				"attributes": {
					"batteryCapacity": "private double batteryCapacity",
					"currentCharge": "private double currentCharge",
					"chargingPortType": "private String chargingPortType"
				},
				"methods": {
					"chargeBattery": "void chargeBattery(doublehours)",
					"regeneratePower": "void regeneratePower(doubleenergy)"
				}
			}
		}

		res = agent.generateCodeByDescriptions(descriptions, classes)
		print(res)
		for file in res:
			file_name = file[0] + ".java"
			# print(file_name)
			file_contents = file[1]
			# print(file_contents)
			write_path = "Convert-UML-to-Java-code/examples/example_code" + f"/{file_name}"
			if os.path.exists(write_path):
				with open(write_path, "w") as f:
					f.write(file_contents)
			else:
				os.makedirs(os.path.dirname(write_path), exist_ok=True)
				with open(write_path, "x") as f:
					f.write(file_contents)