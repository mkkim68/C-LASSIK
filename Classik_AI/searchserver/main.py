from typing import Union

from fastapi import FastAPI
from pydantic import BaseModel
from service import search
from database import make_db

app = FastAPI()


@app.get("/")
def read_root():
    return {"this is": "fast api"}

@app.on_event("startup")
def initialize_db():
    make_db()
    print("서버 시작시 최초로 실행될 DB초기화 코드")

@app.get("/search/embed/{search_item}")
def update_item(search_item: str):
    items = search(search_item)
    return {"items" : items}
