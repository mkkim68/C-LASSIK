package com.example.classik.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.classik.data.model.RecommendList
import com.example.classik.network.ApiService
import com.example.classik.utils.LocalStorageManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel: ViewModel() {
    private val _recommendlists = MutableLiveData<List<RecommendList>>()
    val recommendlists: LiveData<List<RecommendList>> get() = _recommendlists

//    init {
//        val tracks = listOf(
//            BaseTrackItem(
//                1,
//                "음악 1",
//                "베토벤",
//                "https://classik-bucket.s3.ap-northeast-2.amazonaws.com/_hyAOYMUVDs",
//                listOf("Classical", "Mozart")
//            ),
//            BaseTrackItem(
//                2,
//                "음악 2",
//                "볼프강 아마데우스 모차르트",
//                "https://classik-bucket.s3.ap-northeast-2.amazonaws.com/_hyAOYMUVDs",
//                listOf("Classical", "Mozart")
//            ),
//            BaseTrackItem(
//                2,
//                "음악 3",
//                "라흐마니노프",
//                "https://classik-bucket.s3.ap-northeast-2.amazonaws.com/_hyAOYMUVDs",
//                listOf("Classical", "Mozart")
//            )
//        )
//        _recommendlists.value = listOf(
//            RecommendList(
//                "추천플리1",
//                "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyNDAzMDRfOTEg%2FMDAxNzA5NTI1OTE0MzMx.Mrc3WEKTlDMVkK2U7ngyXFQOHy0kLes_s5jc9ZSML1Yg.J5yHMzwqWukaJ7DxhNTCkCk0_Hg_okInRjAxQJ8-0SMg.JPEG%2Fse%25A3%25DFone%25A3%25DF20240304%25A3%25DF131622%25A3%25DFa.jpg&type=a340",
//                tracks
//            ),
//            RecommendList(
//                "추천플리2",
//                "https://classik-bucket.s3.ap-northeast-2.amazonaws.com/_hyAOYMUVDs",
//                tracks
//            ),
//            RecommendList(
//                "추천플리3",
//                "https://classik-bucket.s3.ap-northeast-2.amazonaws.com/_hyAOYMUVDs",
//                tracks
//            ),
//            RecommendList(
//                "추천플리4",
//                "https://classik-bucket.s3.ap-northeast-2.amazonaws.com/_hyAOYMUVDs",
//                tracks
//            ),
//            RecommendList(
//                "추천플리5",
//                "https://classik-bucket.s3.ap-northeast-2.amazonaws.com/_hyAOYMUVDs",
//                tracks
//            ),
//        )
//    }

    fun fetchRecommendLists(context: Context) {
        val authorization = LocalStorageManager.getAccessToken(context)
        ApiService.homeApi.getRecommends("Bearer $authorization").enqueue(object : Callback<List<RecommendList>> {
            override fun onResponse(call: Call<List<RecommendList>>, response: Response<List<RecommendList>>) {
                if (response.isSuccessful) {
                    _recommendlists.value = response.body()
                    Log.d("HomeViewModel", "성공 ${response.body()}")
                } else {
                    Log.d("HomeViewModel", "실패 $response")
                }
            }

            override fun onFailure(call: Call<List<RecommendList>>, t: Throwable) {
                // 네트워크 또는 기타 오류 처리
                Log.d("HomeViewModel", t.toString())
            }
        })
    }

    private val _test = MutableLiveData<String>()
    val test: LiveData<String> get() = _test

    fun fetchTest() {
        ApiService.userApi.getTest().enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    _test.value = response.body()
                    Log.d("dragonMK", "성공")
                } else {
                    Log.d("dragonMK", "실패")
                    Log.d("dragonMK", response.toString())
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                // 네트워크 또는 기타 오류 처리
                Log.d("dragonMK", "네트워크 오류")
                Log.d("dragonMK", t.toString())
            }
        })
    }
}