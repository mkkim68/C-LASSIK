import openai
import chromadb
import os
from dotenv import load_dotenv
import numpy as np

# 환경 변수에서 OpenAI API 키 로드
load_dotenv()
OPENAI_API_KEY = os.getenv("OPENAI_API_KEY")
openai.api_key = OPENAI_API_KEY

# ChromaDB 클라이언트 및 컬렉션 설정
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


def save_to_chromadb(file_path):
    # 파일을 읽어 각 항목을 벡터화하고 ChromaDB에 저장
    with open(file_path, "r", encoding="utf-8") as file:
        for line in file:
            title, composer = line.strip().split("\t")
            embedding = embed_text(f"{title} by {composer}")
            document_id = f"{title}_{composer}"  # 각 항목에 고유 ID 생성
            
            # ChromaDB에 데이터 추가
            collection.add(
                documents=[f"{title} by {composer}"],
                embeddings=[embedding],
                metadatas=[{"title": title, "composer": composer}],
                ids=[document_id]
            )
    print("Data successfully saved to ChromaDB.")

def find_most_similar(query):
    # 입력 쿼리를 벡터화하고 ChromaDB에서 가장 유사한 항목을 검색
    query_embedding = embed_text(query)
    
    # ChromaDB에서 유사한 항목을 찾기 위한 쿼리 실행
    results = collection.query(
        query_embeddings=[query_embedding],
        n_results=5  # 가장 유사한 결과 5개만 반환
    )
    
    # 결과 반환
    if results["documents"]:
        most_similar = results["documents"][0][0]
        metadata = results["metadatas"][0][0]
        return {
            "title": metadata["title"],
            "composer": metadata["composer"],
            "similarity": results["distances"][0][0]
        }
    else:
        return {"error": "No similar items found."}

# 데이터 저장 및 유사도 검색
save_to_chromadb("./datasets/music.txt")

# 검색어 입력 및 유사한 항목 찾기
query = input("Enter a search term: ")
result = find_most_similar(query)
print("Most similar result:", result)
