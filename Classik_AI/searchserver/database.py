import chromadb
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

# ChromaDB 클라이언트 및 컬렉션 설정
chroma_client = chromadb.Client()
collection = chroma_client.get_or_create_collection(name="music_db")

def make_db():
    save_to_chromadb("./datasets/tracks.txt")
    if collection.count() > 0:
        return True
    else:
        return "error"

def embed_text(text):
    # 최신 OpenAI API 응답 형식에 맞춰 수정
    response = openai.embeddings.create(
        model="text-embedding-ada-002",
        input=text
    )
    # 응답 객체의 .data 속성에서 직접 접근
    embedding = response.data[0].embedding
    return embedding


def save_to_chromadb(file_path):
    t1 = time.time()
    # 파일을 읽어 각 항목을 벡터화하고 ChromaDB에 저장
    with open(file_path, "r", encoding="utf-8") as file:
        for line in file:
            title, composer, tags = line.strip().split("\t")
            tags = ''.join(tags[:-1])

            embedding = embed_text(f"{title} by {composer}")
            document_id = f"{title}_{composer}"  # 각 항목에 고유 ID 생성
            
            # ChromaDB에 데이터 추가
            collection.add(
                documents=[f"{title} by {composer}"],
                embeddings=[embedding],
                metadatas=[{"title": title, "composer": composer, 'tags':tags }],
                ids=[document_id]
            )
            
    t2 = time.time()
    print(f"ChromaDB저장 시간 : {t2-t1}" )
    print("Data successfully saved to ChromaDB.")


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
