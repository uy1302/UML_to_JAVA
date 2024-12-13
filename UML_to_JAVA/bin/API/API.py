from fastapi import FastAPI, Request
from typing import Dict

app = FastAPI()

# Temporary storage (optional)
data_store = []
descriptions_store = []
classes_store = []
code_store = []

@app.post("/send_data")
async def receive_data(request: Request):
    data: Dict = await request.json()
    code_store.append(data)  # Save to in-memory store (for testing)
    print(f"Received data: {data}")  # Log to console
    return {"status": "success", "received_data": data}

@app.post("/send_descriptions")
async def receive_descriptions(request: Request):
    data: Dict = await request.json()
    descriptions_store.append(data)  # Save to in-memory store (for testing)
    print(f"Received data: {data}")  # Log to console
    return {"status": "success", "received_descriptions": data}

@app.post("/send_classes")
async def receive_descriptions(request: Request):
    data: Dict = await request.json()
    classes_store.append(data)  # Save to in-memory store (for testing)
    print(f"Received data: {data}")  # Log to console
    return {"status": "success", "received_classes": data}

@app.post("/clear_data")
async def clear_data():
    data_store.clear()
    descriptions_store.clear()
    classes_store.clear()
    code_store.clear()
    return {"status": "success", "received_data": data_store}

@app.get("/get_descriptions")
async def get_data():
    """Retrieve all stored data."""
    return {"all_data": descriptions_store}

@app.get("/get_code")
async def get_data():
    """Retrieve all stored data."""
    return {"all_data": code_store}

@app.get("/")
def read_root():
    return {"Hello": "World"}


# @app.get("/classes")
# def send_data(file_name: str, file_content: str):
#     return {"file_name": file_name, "file_content": file_content}