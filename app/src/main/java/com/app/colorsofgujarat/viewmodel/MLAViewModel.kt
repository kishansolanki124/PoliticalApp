package com.app.colorsofgujarat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.colorsofgujarat.network.APIEndPointsInterface
import com.app.colorsofgujarat.network.RetrofitFactory
import com.app.colorsofgujarat.pojo.GiveMLARatingResponse
import com.app.colorsofgujarat.pojo.MLADetailResponse
import com.app.colorsofgujarat.pojo.MLAListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class MLAViewModel : ViewModel() {

    private val mutableSettingsResponse = MutableLiveData<MLAListResponse>()
    private val mutableMLADetailResponse = MutableLiveData<MLADetailResponse>()
    private val mutableMLAVoteResponse = MutableLiveData<GiveMLARatingResponse>()
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

                val apiResponse = apiEndPointsInterface.mlaList(
                    requestBodyBuilder.build()
                )
                returnSettingsResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getMLADetail(gid: String, user_mobile: String) {
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

                val apiResponse = apiEndPointsInterface.mlaDetail(
                    requestBodyBuilder.build()
                )
                returnMLADetailResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun giveMLARating(gid: String, user_mobile: String, pid: String, user_rating: String) {
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
                    AppConstants.RequestParameters.pid,
                    pid
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.user_rating,
                    user_rating
                )

                val apiResponse = apiEndPointsInterface.giveMLARating(
                    requestBodyBuilder.build()
                )
                returnMLARating(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun returnSettingsResponse(settingsResponse: MLAListResponse) {
        withContext(Dispatchers.Main) {
            mutableSettingsResponse.value = settingsResponse
        }
    }

    private suspend fun returnMLADetailResponse(settingsResponse: MLADetailResponse) {
        withContext(Dispatchers.Main) {
            mutableMLADetailResponse.value = settingsResponse
        }
    }

    private suspend fun returnMLARating(settingsResponse: GiveMLARatingResponse) {
        withContext(Dispatchers.Main) {
            mutableMLAVoteResponse.value = settingsResponse
        }
    }

    fun govtWorkList(): LiveData<MLAListResponse> {
        return mutableSettingsResponse
    }

    fun mLAVoteResponse(): LiveData<GiveMLARatingResponse> {
        return mutableMLAVoteResponse
    }

    fun mlaDetail(): LiveData<MLADetailResponse> {
        return mutableMLADetailResponse
    }
}