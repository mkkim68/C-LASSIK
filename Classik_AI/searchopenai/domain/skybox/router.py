from fastapi import APIRouter, HTTPException
from .schemas import SkyboxRequest, SkyboxResponse
from .service import create_skybox_image

router = APIRouter()

@router.post("/skybox", response_model=SkyboxResponse)
async def generate_skybox(request: SkyboxRequest):
    try:
        image_url = await create_skybox_image(request.content)
        return SkyboxResponse(image_url=image_url)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
