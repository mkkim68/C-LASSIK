from langchain_openai.embeddings import OpenAIEmbeddings
import chromadb
import uuid
import os
from dotenv import load_dotenv

# .env 파일에서 API 키 로드
load_dotenv()
API_KEY = os.getenv("OPENAI_API_KEY")

# embedding 모델 초기화
embeddings = OpenAIEmbeddings(
    model="text-embedding-ada-002",
    openai_api_key=API_KEY  # API 키 전달
)

chroma_client = chromadb.Client()
collection = chroma_client.get_or_create_collection(name="music_db")

titles=[]
vector_titles = []
composers=[]
vector_composers = []

# 음악 데이터를 벡터화하여 ChromaDB에 저장
with open("music.txt", "r", encoding="utf-8") as file:
    for line in file:
        data = line.strip().split("\t")
        
        if len(data) == 2:
            # title, composer을 각각 분리한 후, vectorization
            title, composer = data
            titles.append(title)
            composers.append(composer)
            # 각각 벡터 생성
            t_vector = embeddings.embed_query(composer)
            c_vector = embeddings.embed_query(composer)
            vector_titles.append(t_vector)
            vector_composers.append(c_vector)
            


# ChromaDB에 데이터 추가
collection.add(
    ids=[f"title {uuid.uuid4()}" for _ in range(len(titles))],
    documents=titles,
    embeddings=vector_titles
)
collection.add(
    ids=[f"composers {uuid.uuid4()}" for _ in range(len(composers))],
    documents=composers,
    embeddings=vector_composers
)
print("완성~")
