from src.genAI.AIgenerator import AIagent
import os 
import requests
import urllib.parse
import time 

agent = AIagent(api_key="AIzaSyD7UJTPn6veFbiKhgDydYTgrDR14D9Grf4")

url = "http://127.0.0.1:8000"
response = requests.get(
        f"{url}/get_descriptions", 
    )
descriptions = response.json()['all_data'][0]["descriptions"]
classes = response.json()['all_data'][0]["classes"]
print(descriptions)
print(classes)
res = agent.generateCodeByDescriptions(descriptions, classes)
# print(res)
for file in res:
    file_name = file[0] + ".java"
    print(file_name)
    file_contents = file[1]
    print(file_contents)
   # write_path = "examples/example_code" + f"/{file_name}"
   # if os.path.exists(write_path):
      #  with open(write_path, "w") as f:
           # f.write(file_contents)
   # else:
       # os.makedirs(os.path.dirname(write_path), exist_ok=True)
       # with open(write_path, "x") as f:
           # f.write(file_contents)
    # print(file[0])
    # print(file_contents)
    # print(url)
    response = requests.post(
        f"{url}/send_data", 
        json={f"{file_name}": file_contents}
    )
    # print("Response:", response.text)