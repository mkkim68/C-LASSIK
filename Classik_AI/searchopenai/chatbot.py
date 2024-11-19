import openai
from openai import OpenAI
from dotenv import load_dotenv
import os
from vectornorm import collection 

# .env 파일에서 API 키 로드
load_dotenv()
API_KEY = os.getenv("OPENAI_API_KEY")

# openai API 키 설정
openai.api_key = API_KEY

def text_embed(content_input):
    client = OpenAI()
    # 벡터화를 위한 언어모델인 text-embedding-ada-002사용
    response = client.embeddings.create(
        model="text-embedding-ada-002",
        input=content_input
    )
    # input 의 벡터화 
    input_vectorized = response.data[0].embedding
    
    return input_vectorized


def find_most_similar(query_text):
    # 입력 텍스트를 벡터화
    query_vector = text_embed(query_text)
    
    # 유사도 기반으로 ChromaDB에서 가장 유사한 항목 찾기
    results = collection.query(
        query_embeddings=[query_vector],
        n_results=1
    )
    
    # # 검색 결과가 존재하는지 확인
    # if results["documents"] and results["metadatas"]:
    #     most_similar = results["documents"][0][0]
    #     metadata = results["metadatas"][0][0]
        
    #     print("가장 유사한 결과:")
    #     print(f"음악 제목: {metadata['title']}")
    #     print(f"작곡가: {metadata['composer']}")
    #     print(f"내용: {most_similar}")
    # else:
    #     print("유사한 결과를 찾지 못했습니다.")

    
query = input()
find_most_similar(query)