package com.politics.politicalapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.app.patidarsaurabh.apputils.AppConstants
import com.politics.politicalapp.network.APIEndPointsInterface
import com.politics.politicalapp.network.RetrofitFactory
import com.politics.politicalapp.pojo.GiveUserRatingToGovtWorkResponse
import com.politics.politicalapp.pojo.GovtWorkDetailResponse
import com.politics.politicalapp.pojo.GovtWorkListResponse
import com.politics.politicalapp.pojo.NewsDetailResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class GovtWorkViewModel : ViewModel() {

    private val mutableSettingsResponse = MutableLiveData<GovtWorkListResponse>()
    private val mutableGovtWorkDetailResponse = MutableLiveData<GovtWorkDetailResponse>()
    private val newsDetailResponse = MutableLiveData<NewsDetailResponse>()
    private val mutableGiveUserRatingToGovtWorkResponse =
        MutableLiveData<GiveUserRatingToGovtWorkResponse>()
    private val mutableGovtWorkCommentResponse =
        MutableLiveData<GiveUserRatingToGovtWorkResponse>()
    private var apiEndPointsInterface =
        RetrofitFactory.createService(APIEndPointsInterface::class.java)

    fun getGovtWorkList(district_id: String, start: String, end: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.district_id,
                    district_id
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.end,
                    end
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.start,
                    start
                )

                val apiResponse = apiEndPointsInterface.getGovtWork(
                    requestBodyBuilder.build()
                )
                returnSettingsResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun giveUserRating(gid: String, user_mobile: String, user_rating: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.gid,
                    gid
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.user_mobile,
                    user_mobile
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.user_rating,
                    user_rating
                )

                val apiResponse = apiEndPointsInterface.addRatingGovtWork(
                    requestBodyBuilder.build()
                )
                returnGiveUserRatingToGovtWorkResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addGovtWorkComment(gid: String, user_mobile: String, user_comment: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.gid,
                    gid
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.user_mobile,
                    user_mobile
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.user_comment,
                    user_comment
                )

                val apiResponse = apiEndPointsInterface.addGovtWorkComment(
                    requestBodyBuilder.build()
                )
                returnGovtWorkCommentResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getGovtWorkDetail(gid: String, user_mobile: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.gid,
                    gid
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.user_mobile,
                    user_mobile
                )

                val apiResponse = apiEndPointsInterface.getGovtWorkDetail(
                    requestBodyBuilder.build()
                )
                returnGovtWorkDetailResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getNewsDetail(nid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.nid,
                    nid
                )

                val apiResponse = apiEndPointsInterface.getNewsDetail(
                    requestBodyBuilder.build()
                )
                returnNewsDetailResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun returnSettingsResponse(settingsResponse: GovtWorkListResponse) {
        withContext(Dispatchers.Main) {
            mutableSettingsResponse.value = settingsResponse
        }
    }

    private suspend fun returnGiveUserRatingToGovtWorkResponse(apiResponse: GiveUserRatingToGovtWorkResponse) {
        withContext(Dispatchers.Main) {
            mutableGiveUserRatingToGovtWorkResponse.value = apiResponse
        }
    }

    private suspend fun returnGovtWorkCommentResponse(apiResponse: GiveUserRatingToGovtWorkResponse) {
        withContext(Dispatchers.Main) {
            mutableGovtWorkCommentResponse.value = apiResponse
        }
    }

    fun govtWorkList(): LiveData<GovtWorkListResponse> {
        return mutableSettingsResponse
    }

    fun userRating(): LiveData<GiveUserRatingToGovtWorkResponse> {
        return mutableGiveUserRatingToGovtWorkResponse
    }

    fun newComment(): LiveData<GiveUserRatingToGovtWorkResponse> {
        return mutableGovtWorkCommentResponse
    }

    private suspend fun returnGovtWorkDetailResponse(settingsResponse: GovtWorkDetailResponse) {
        withContext(Dispatchers.Main) {
            mutableGovtWorkDetailResponse.value = settingsResponse
        }
    }

    private suspend fun returnNewsDetailResponse(settingsResponse: NewsDetailResponse) {
        withContext(Dispatchers.Main) {
            newsDetailResponse.value = settingsResponse
        }
    }

    fun govtWorkDetail(): LiveData<GovtWorkDetailResponse> {
        return mutableGovtWorkDetailResponse
    }

    fun newsDetail(): LiveData<NewsDetailResponse> {
        return newsDetailResponse
    }
}