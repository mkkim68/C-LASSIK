import openai
from openai import OpenAI
import chromadb
import os
import time 
from dotenv import load_dotenv
import numpy as np

# 환경 변수에서 OpenAI API 키 로드
load_dotenv()
OPENAI_API_KEY = os.getenv("OPENAI_API_KEY")
openai.api_key = OPENAI_API_KEY
client = OpenAI()


chroma_client = chromadb.Client()
collection = chroma_client.get_or_create_collection(name="music_db")

def embed_text(text):
    # 최신 OpenAI API 응답 형식에 맞춰 수정
    response = openai.embeddings.create(
        model="text-embedding-ada-002",
        input=text
    )
    # 응답 객체의 .data 속성에서 직접 접근
    embedding = response.data[0].embedding
    return embedding


def find_most_similar(query):
    # 입력 쿼리를 벡터화하고 ChromaDB에서 가장 유사한 항목을 검색
    query_embedding = embed_text(query)
    
    # ChromaDB에서 유사한 항목을 찾기 위한 쿼리 실행
    results = collection.query(
        query_embeddings=[query_embedding],
        n_results=20  # 가장 유사한 결과 20개만 반환
    )
    
    # 결과 반환
    if results["documents"]:
        # most_similar = results["documents"][0][0]
        # metadata = results["metadatas"][0][0]
        return results["metadatas"]

    else:
        return {"error": "No similar items found."}

# 문법 오류 수정

def grammarly(text):
    response = client.chat.completions.create(
        model="gpt-4o",
        messages=[
            {
            "role": "system",
            "content": (
                "You will be provided with statements or words, and your task is to convert them to standard English."
                "remember this statement is kind of music title or name of composer"
                "if text language is not English it should be return in English"
            )
            },
            {
            "role": "user",
            "content": text
            }
        ],
        temperature=0.7,
        max_tokens=64,
        top_p=1
    )
    return response.choices[0].message.content


def search(search_item):
    query = grammarly(search_item)
    result = find_most_similar(query)
    return result