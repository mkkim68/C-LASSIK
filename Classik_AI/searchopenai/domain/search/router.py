from fastapi import APIRouter, HTTPException, Depends
from .schemas import SearchRequest, SearchResponse
from .service import search_data
from sqlalchemy.orm import Session
from database import get_db

# FastAPI APIRouter 객체 생성
router = APIRouter()

@router.post("/search", response_model=SearchResponse)
async def search_word(request: SearchRequest, db: Session = Depends(get_db)):
    try:
        result = await search_data(request.query, db)
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
