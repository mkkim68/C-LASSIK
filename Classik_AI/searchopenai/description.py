import pymysql
import openai
from openai import OpenAI
from dotenv import load_dotenv
import os

# key 관리
load_dotenv()
client = OpenAI()
openai.api_key = os.getenv("OPENAI_API_KEY")


# DB 연결
classik = pymysql.connect(host='127.0.0.1', user='root', password='0516', db='classik', charset='utf8')
cursor = classik.cursor()

def make_prompt(title):
    response = client.chat.completions.create(
        model="gpt-4o",
        messages=[
            {
                "role": "system",
                "content": (
                    "You are a classic music curator"
                    "Use the following guidelines:\n\n"
                    "You are a classic music curator. Please follow the provided guidelines to introduce the music. "
                    "Based on the given title and composer's name, write a 2-sentence introduction that includes the historical context, any special story related to the music, its mood, and how it can be enjoyed. "
                    "The introduction must be in Korean and absolutely free of any incorrect information. "
                    "Make sure to highlight the essential characteristics and emotions of the music."
                )
            },
            {
                "role": "user",
                "content": title 
            }
        ],
        temperature=0.6,
        max_tokens=300,
        top_p=1
    )
    return response.choices[0].message.content



def update_description(video_id, description):
    try:
        sql = "UPDATE track SET description = %s WHERE video_id = %s"
        cursor.execute(sql, (description, video_id))
        classik.commit()
        print(f"Updated description for track ID {video_id}")
    except Exception as e:
        print("Failed to update database:", e)



if __name__ == '__main__':
    # track 테이블의 title을 가져와 Skybox 생성 및 저장
    cursor.execute("SELECT track_id, video_id, title FROM track")
    tracks = cursor.fetchall()
    
    for track_id, video_id, title in tracks:
        comment = make_prompt(title)
        print(comment)

        if comment:
            update_description(video_id, comment)
        else:
            print("데이터 입력 실패:", track_id)
        print(track_id, " done!")