import openai
from dotenv import load_dotenv
import os
from sqlalchemy.orm import Session

# .env 파일에서 API 키 로드
load_dotenv()
API_KEY = os.getenv("OPENAI_API_KEY")
openai.api_key = API_KEY

async def search_data(query: str, db: Session):
    # OpenAI API를 사용해 벡터화 및 검색 수행
    response = openai.Embedding.create(
        model="text-embedding-ada-002",
        input=query
    )
    query_vector = response['data'][0]['embedding']
    
    # 여기서 db 또는 캐시에 저장된 벡터와 유사도를 계산하여 관련 단어를 반환
    results = ["related_word_1", "related_word_2"]  # 실제로는 db에서 검색한 결과
    
    return {"results": results}
