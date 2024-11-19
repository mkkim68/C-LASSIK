from pydantic import BaseModel

class SkyboxRequest(BaseModel):
    content: str  # 감상 문장 (500자 이내의 영문)

class SkyboxResponse(BaseModel):
    image_url: str
