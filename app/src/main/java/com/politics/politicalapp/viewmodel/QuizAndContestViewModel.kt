package com.politics.politicalapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.app.patidarsaurabh.apputils.AppConstants
import com.politics.politicalapp.network.APIEndPointsInterface
import com.politics.politicalapp.network.RetrofitFactory
import com.politics.politicalapp.pojo.CommonResponse
import com.politics.politicalapp.pojo.QuizAndContestDynamicResponse
import com.politics.politicalapp.pojo.QuizAndContestResponse
import com.politics.politicalapp.pojo.QuizAndContestRunningResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class QuizAndContestViewModel : ViewModel() {

    private val mutableQuizAndContestResponse = MutableLiveData<QuizAndContestResponse>()
    private val mutableQuizAndContestDynamicResponse = MutableLiveData<QuizAndContestDynamicResponse>()
    private val mutableQuizAndContestDetailResponse =
        MutableLiveData<QuizAndContestRunningResponse>()
    private val mutableCommonResponse = MutableLiveData<CommonResponse>()

    private var apiEndPointsInterface =
        RetrofitFactory.createService(APIEndPointsInterface::class.java)

    fun getQuizAndContest(start: String, end: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.start,
                    start
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.end,
                    end
                )

                val apiResponse = apiEndPointsInterface.getQuizAndContest(
                    requestBodyBuilder.build()
                )
                returnQuizAndContestResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getQuizAndContestDynamic() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiResponse = apiEndPointsInterface.getQuizAndContestDynamic()
                returnQuizAndContestDynamicResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getQuizAndContestDetail(qid: String, user_mobile: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.qid,
                    qid
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.user_mobile,
                    user_mobile
                )

                val apiResponse = apiEndPointsInterface.getQuizAndContestDetail(
                    requestBodyBuilder.build()
                )
                returnQuizAndContestDetailResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addPhotoContestUser(user_mobile: String, cid: String, selectedFile: File) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.user_mobile,
                    user_mobile
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.cid,
                    cid
                )

                val requestBody: RequestBody =
                    selectedFile.asRequestBody("image/*".toMediaTypeOrNull())

                requestBodyBuilder.addFormDataPart(
                    "up_pro_img", "up_pro_img.jpg", requestBody
                )

                val apiResponse = apiEndPointsInterface.addPhotoContest(
                    requestBodyBuilder.build()
                )
                returnQuizAndContestAnswerResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addQuiZAnswer(qid: String, user_mobile: String, user_answer: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.qid,
                    qid
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.user_mobile,
                    user_mobile
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.user_answer,
                    user_answer
                )

                val apiResponse = apiEndPointsInterface.addQuiZAnswer(
                    requestBodyBuilder.build()
                )
                returnQuizAndContestAnswerResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun returnQuizAndContestAnswerResponse(apiResponse: CommonResponse) {
        withContext(Dispatchers.Main) {
            mutableCommonResponse.value = apiResponse
        }
    }

    private suspend fun returnQuizAndContestResponse(scratchCardResponse: QuizAndContestResponse) {
        withContext(Dispatchers.Main) {
            mutableQuizAndContestResponse.value = scratchCardResponse
        }
    }

    private suspend fun returnQuizAndContestDynamicResponse(scratchCardResponse: QuizAndContestDynamicResponse) {
        withContext(Dispatchers.Main) {
            mutableQuizAndContestDynamicResponse.value = scratchCardResponse
        }
    }

    private suspend fun returnQuizAndContestDetailResponse(scratchCardResponse: QuizAndContestRunningResponse) {
        withContext(Dispatchers.Main) {
            mutableQuizAndContestDetailResponse.value = scratchCardResponse
        }
    }


    fun quizAndContest(): LiveData<QuizAndContestResponse> {
        return mutableQuizAndContestResponse
    }

    fun quizAndContestDetail(): LiveData<QuizAndContestRunningResponse> {
        return mutableQuizAndContestDetailResponse
    }

    fun quizAndContestAnswer(): LiveData<CommonResponse> {
        return mutableCommonResponse
    }

    fun quizAndContestDynamic(): LiveData<QuizAndContestDynamicResponse> {
        return mutableQuizAndContestDynamicResponse
    }
}