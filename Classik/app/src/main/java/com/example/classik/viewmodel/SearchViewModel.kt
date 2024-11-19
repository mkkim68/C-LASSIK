package com.example.classik.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.classik.data.model.BaseTrackItem
import com.example.classik.data.model.SearchHistory
import com.example.classik.network.ApiService
import com.example.classik.utils.LocalStorageManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel: ViewModel() {
    private val _searchHistory = MutableLiveData<List<String>>()
    val searchHistory: LiveData<List<String>> get() = _searchHistory

    private val _searchResult = MutableLiveData<List<BaseTrackItem>>()
    val searchResult: LiveData<List<BaseTrackItem>> get() = _searchResult

    private val _searchSuccess = MutableLiveData<Boolean>()
    val searchSuccess: LiveData<Boolean> get() = _searchSuccess


    fun fetchSearchHistory(context: Context) {
        val authorization = LocalStorageManager.getAccessToken(context)
        ApiService.searchApi.getSearchHistory("Bearer $authorization").enqueue(object : Callback<SearchHistory> {
            override fun onResponse(call: Call<SearchHistory>, response: Response<SearchHistory>) {
                if (response.isSuccessful) {
                    _searchHistory.value = response.body()!!.searchHistory
//                    Log.d("Search History", "성공: ${response.body()}")
                } else {
                    Log.d("Search History", "실패: $response")

                }
            }

            override fun onFailure(call: Call<SearchHistory>, t: Throwable) {
                Log.d("Search History", "오류: $t")
            }
        })
    }

    fun fetchSearch(context: Context, keyword: String, onSuccess: () -> Unit) {
        val authorization = LocalStorageManager.getAccessToken(context)
        ApiService.searchApi.getSearch("Bearer $authorization", keyword).enqueue(object : Callback<List<BaseTrackItem>> {
            override fun onResponse(call: Call<List<BaseTrackItem>>, response: Response<List<BaseTrackItem>>) {
                if (response.isSuccessful) {
                    _searchResult.value = response.body()
                    _searchSuccess.value = true
//                    Log.d("Search", "성공: ${response.body()}")
                    onSuccess() // 성공 시 콜백 호출
                } else {
                    _searchSuccess.value = false
                    Log.d("Search", "실패: $response")
                }
            }

            override fun onFailure(call: Call<List<BaseTrackItem>>, t: Throwable) {
                Log.d("Search", "오류: $t")
            }
        })
    }

}