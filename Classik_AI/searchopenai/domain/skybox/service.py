import httpx
from dotenv import load_dotenv
import os

# .env 파일에서 API 키 로드
load_dotenv()
SKYBOX_API_KEY = os.getenv("SKYBOX_API_KEY")
SKYBOX_API_URL = "https://backend.blockadelabs.com/api/v1/skybox"

async def create_skybox_image(content: str) -> str:
    headers = {
        "x-api-key": SKYBOX_API_KEY,
        "Content-Type": "application/json"
    }
    data = {
        "prompt": content
    }
    
    async with httpx.AsyncClient() as client:
        response = await client.post(SKYBOX_API_URL, headers=headers, json=data)
        
        if response.status_code == 200:
            result = response.json()
            return result.get("file_url")
        else:
            raise Exception("Skybox API request failed")
