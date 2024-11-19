package com.example.classik.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.classik.data.model.BaseTrackItem
import com.example.classik.data.model.LoginResponse
import com.example.classik.data.model.MemberInfo
import com.example.classik.network.ApiService
import com.example.classik.network.LoginRequest
import com.example.classik.network.SignupRequest
import com.example.classik.utils.LocalStorageManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MyPageViewModel : ViewModel() {
    private val _history = MutableLiveData<List<BaseTrackItem>>(emptyList())
    val history: LiveData<List<BaseTrackItem>> get() = _history

    private val _memberInfo = MutableLiveData<MemberInfo>()
    val memberInfo: LiveData<MemberInfo> get() = _memberInfo

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> get() = _loginSuccess

    private val _loginErrorMessage = MutableLiveData<String>()
    val loginErrorMessage: LiveData<String> get() = _loginErrorMessage

    fun setLoginSuccess(value: Boolean) {
        _loginSuccess.value = value
    }

//    init {
//        // mock data
//        _memberInfo.value = MemberInfo(
//            1,
//            "a604@classik.com",
//            "볼프강 아마데우스 모차르트",
//            "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzEyMTVfMzgg%2FMDAxNzAyNTY4OTcxMzkx.aOhOp1ISJdg7HbUFo1gUfYX9kIYDvardHNgzCdsNBwgg.UdU2Hmc3Kyvrmqt9drtXOeJLe-O6B-PfPEu2qaVLQrYg.JPEG.adsfdg1%2FIMG_3422.jpg&type=a340",
//            "member"
//        )
//        _history.value = listOf(
//            BaseTrackItem(
//                1,
//                "Symphony No.5",
//                "Beethoven",
//                "https://classik-bucket.s3.ap-northeast-2.amazonaws.com/9PFGpPzu_7s",
//                listOf("Classical", "Symphony")
//            ),
//            BaseTrackItem(
//                1,
//                "Symphony No.5",
//                "Beethoven",
//                "https://classik-bucket.s3.ap-northeast-2.amazonaws.com/9PFGpPzu_7s",
//                listOf("Classical", "Symphony")
//            ),
//            BaseTrackItem(
//                1,
//                "Symphony No.5",
//                "Beethoven",
//                "https://classik-bucket.s3.ap-northeast-2.amazonaws.com/9PFGpPzu_7s",
//                listOf("Classical", "Symphony")
//            ),
//            BaseTrackItem(
//                1,
//                "Symphony No.5",
//                "Beethoven",
//                "https://classik-bucket.s3.ap-northeast-2.amazonaws.com/9PFGpPzu_7s",
//                listOf("Classical", "Symphony")
//            ),
//            BaseTrackItem(
//                1,
//                "Symphony No.5",
//                "Beethoven",
//                "https://classik-bucket.s3.ap-northeast-2.amazonaws.com/9PFGpPzu_7s",
//                listOf("Classical", "Symphony")
//            ),
//        )
//    }

    private val _signupSuccess = MutableLiveData<Boolean>()
    val signupSuccess: LiveData<Boolean> get() = _signupSuccess

    fun setSignupSuccess(value: Boolean) {
        _signupSuccess.value = value
    }

    private val _signUpErrorMessage = MutableLiveData<String>()
    val signupErrorMessage: LiveData<String> get() = _signUpErrorMessage

    fun signup(
        email: String,
        password: String,
        nickname: String
    ) {
        val userInfo = SignupRequest(email=email, password=password, nickname=nickname, profileImage = null)
        ApiService.noReissueApi.signup(userInfo).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    _signupSuccess.value = true
                } else {
                    _signupSuccess.value = false
                    _signUpErrorMessage.value = "회원가입 실패: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _signupSuccess.value = false
                _signUpErrorMessage.value = "회원가입 요청 실패: ${t.message}"
            }
        })
    }


    fun login(email: String, password: String, context: Context) {
        val loginRequest = LoginRequest(email = email, password = password)
        ApiService.noReissueApi.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    _loginSuccess.value = true
                    _logoutSuccess.value = false
                    _memberInfo.value = response.body()?.userInfo
                    LocalStorageManager.saveAccessToken(context, response.body()!!.accessToken)
                    LocalStorageManager.saveRefreshToken(context, response.body()!!.refreshToken)
                } else {
                    _loginSuccess.value = false
                    _loginErrorMessage.value = response.body().toString()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _loginSuccess.value = false
                _loginErrorMessage.value = t.toString()
            }
        })
    }

    fun fetchHistory(context: Context) {
        val authorization = LocalStorageManager.getAccessToken(context)
        ApiService.noReissueApi.getHistory("Bearer $authorization").enqueue(object : Callback<List<BaseTrackItem>> {
            override fun onResponse(call: Call<List<BaseTrackItem>>, response: Response<List<BaseTrackItem>>) {
                if (response.isSuccessful) {
                    _history.value = response.body()
                    Log.d("History 불러오기", "성공"+response.toString())
                } else {
                    Log.d("History 불러오기", "실패"+response.toString())
                    Log.d("History 불러오기", "실패"+response.headers())
                    Log.d("History 불러오기", "실패"+response.body())
                }
            }

            override fun onFailure(call: Call<List<BaseTrackItem>>, t: Throwable) {
                // 네트워크 또는 기타 오류 처리
                Log.d("History 불러오기", t.toString())
            }
        })
    }

    fun fetchMemberInfo(context: Context, memberIdRequest: Int) {
        val authorization = LocalStorageManager.getAccessToken(context)
        Log.d("fetchMemberInfo", "유저 정보 fetch 호출함")
        ApiService.myPageApiService.getMemberInfo(authorization = "Bearer $authorization", memberIdRequest).enqueue(object : Callback<MemberInfo> {
            override fun onResponse(call: Call<MemberInfo>, response: Response<MemberInfo>) {
                if (response.isSuccessful) {
                    // 성공적으로 회원 정보를 가져왔을 때 처리
                    _memberInfo.value = response.body()
                    Log.d("MEMBERINFO", "info: ${response.body()}")
                } else {
                    // 에러 처리 (예: 인증 실패 또는 서버 에러)
                    Log.e("fetchMemberInfo", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<MemberInfo>, t: Throwable) {
                // 네트워크 또는 기타 오류 처리
                Log.e("fetchMemberInfo", "Network error: ${t.message}")
            }
        })
    }

    fun fetchPatchMember(
        context: Context,
        memberId: Int,
        password: String?,
        nickname: String?,
        profileImageUri: Uri?
    ) {
        val params = mutableMapOf<String, RequestBody>()

        password?.takeIf { it.isNotBlank() }?.let {
            params["password"] = RequestBody.create("text/plain".toMediaTypeOrNull(), it)
        }

        nickname?.takeIf { it.isNotBlank() }?.let {
            params["nickname"] = RequestBody.create("text/plain".toMediaTypeOrNull(), it)
        }

        Log.d("fetchPatchMember", "$params")

        val profileImagePart = profileImageUri?.let { uri: Uri ->
            val file = File(uri.path)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("profileImage", file.name, requestFile)
        }
        val authorization = LocalStorageManager.getAccessToken(context)

        ApiService.myPageApiService.patchMemberInfo(
            authorization = "Bearer $authorization",
            memberId = memberId,
            params = params,
            profileImage = profileImagePart
        ).enqueue(object : Callback<MemberInfo> {
            override fun onResponse(call: Call<MemberInfo>, response: Response<MemberInfo>) {
                if (response.isSuccessful) {
                    // 성공적으로 응답을 받았을 때의 로직 처리
                    _memberInfo.value = response.body()
                    // 필요한 경우 UI 업데이트 로직 추가

                    Log.d("MEMBERINFO", "info: ${response.body()}")
                } else {
                    // 에러 응답 처리
                    Log.e("fetchPatchMember", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<MemberInfo>, t: Throwable) {
                // 네트워크 실패 처리
                Log.e("fetchPatchMember", "Network Failure: ${t.message}")
            }
        })
    }

    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean> get() = _deleteSuccess


    fun fetchDeleteMember(context: Context, memberIdRequest: Int) {
        val authorization = LocalStorageManager.getAccessToken(context)
        ApiService.myPageApiService.deleteMember("Bearer $authorization", memberIdRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    _deleteSuccess.value = true
                    _loginSuccess.value = false
                    LocalStorageManager.clearTokens(context)
                } else {
                    // 에러 처리 (예: 인증 실패 또는 서버 에러)
                    Log.e("Delete Member", "Error: ${response.errorBody()?.string()}")
                    _deleteSuccess.value = false
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // 네트워크 또는 기타 오류 처리
                Log.e("Delete Member", "Network error: ${t.message}")
            }
        })
    }

    private val _logoutSuccess = MutableLiveData<Boolean>()
    val logoutSuccess: LiveData<Boolean> get() = _logoutSuccess

    fun fetchLogout(context: Context) {
        val authorization = LocalStorageManager.getAccessToken(context)
        val refresh = LocalStorageManager.getRefreshToken(context)

//        val header = HashMap<String, String>()
//        header["Authorization"] = "Bearer $authorization"
//        header["Cookie"] = "refresh=$refresh"

        Log.d("Logout", "Bearer $authorization")
        Log.d("Logout", "refresh=$refresh")
        ApiService.userApi.logout("Bearer $authorization", "refresh=$refresh").enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    _logoutSuccess.value = true
                    _loginSuccess.value = false
                    LocalStorageManager.clearTokens(context)
                    Log.d("Logout", "성공: $response")
                } else {
                    _logoutSuccess.value = false
                    Log.d("Logout", "실패: $response")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _logoutSuccess.value = false
                Log.d("Logout", "fail: $t")
            }
        })
    }
}