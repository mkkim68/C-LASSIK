from fastapi import FastAPI
from domain.search import router as search_router
from domain.skybox import router as skybox_router

app = FastAPI()

# 각 도메인 라우터에 prefix를 추가하여 등록
app.include_router(search_router)
app.include_router(skybox_router, prefix="/api/v1/skybox", tags=["skybox"])