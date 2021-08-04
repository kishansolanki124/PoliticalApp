package com.app.colorsofgujarat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.colorsofgujarat.R
import com.app.colorsofgujarat.apputils.showSnackBar
import com.app.colorsofgujarat.network.APIEndPointsInterface
import com.app.colorsofgujarat.network.RetrofitFactory
import com.app.colorsofgujarat.pojo.CommonResponse
import com.app.colorsofgujarat.pojo.NotificationResponse
import com.app.colorsofgujarat.pojo.UserAdviseDetailResponse
import com.app.colorsofgujarat.pojo.UserAdviseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class UserAdviseViewModel : ViewModel() {

    private val mutableSettingsResponse = MutableLiveData<UserAdviseResponse>()
    private val mutableNotificationResponse = MutableLiveData<NotificationResponse>()
    private val commonResponse = MutableLiveData<CommonResponse>()
    private val mutableUserAdviseDetailResponse = MutableLiveData<UserAdviseDetailResponse>()

    private var apiEndPointsInterface =
        RetrofitFactory.createService(APIEndPointsInterface::class.java)

    fun getUserAdviseList(district_id: String, start: String, end: String) {
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

                val apiResponse = apiEndPointsInterface.getUserAdvise(
                    requestBodyBuilder.build()
                )
                returnSettingsResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getNotification(user_mobile: String, start: String, end: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.user_mobile,
                    user_mobile
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.end,
                    end
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.start,
                    start
                )

                val apiResponse = apiEndPointsInterface.getNotification(
                    requestBodyBuilder.build()
                )
                returnNotificationResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addUserAdvice(
        district_id: String, user_mobile: String, city: String, title: String, description: String,
        selectedFile: File?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.district_id,
                    district_id
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.user_mobile,
                    user_mobile
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.city,
                    city
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.title,
                    title
                )
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.description,
                    description
                )

                if (null != selectedFile) {
                    val requestBody: RequestBody =
                        selectedFile.asRequestBody("image/*".toMediaTypeOrNull())

                    requestBodyBuilder.addFormDataPart(
                        "up_pro_img", "up_pro_img.jpg", requestBody
                    )
                }

//                val requestBody: RequestBody =
//                    selectedFile.asRequestBody("*/*".toMediaTypeOrNull())

//                val fileToUpload: MultipartBody.Part =
//                    MultipartBody.Part.createFormData("file", selectedFile.name, requestBody)

//                val requestBody: RequestBody =
//                    selectedFile.asRequestBody("image/*".toMediaTypeOrNull())

//                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

//                val filename: RequestBody =
//                    selectedFile.name.toRequestBody("text/plain".toMediaTypeOrNull())

//                requestBodyBuilder.addFormDataPart(
//                    AppConstants.RequestParameters.up_pro_img,
//                    "xyz", filename
//                )

//                val filePart: MultipartBody.Part = MultipartBody.Part.createFormData(
//                    "up_pro_img",
//                    selectedFile.name,
//                    selectedFile.asRequestBody("image/*".toMediaTypeOrNull())
//                )

//                val apiResponse = apiEndPointsInterface.addUserAdvise(
//                    district_id, user_mobile, city, title, description,
//                    //fileToUpload, filename
//                    //requestBody
//                    filePart
//                )

                val apiResponse = apiEndPointsInterface.addUserAdvise(
                    requestBodyBuilder.build()
                )

                returnCommonResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getUserAdviseDetail(aid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.aid,
                    aid
                )
                val apiResponse = apiEndPointsInterface.getUserAdviseDetail(
                    requestBodyBuilder.build()
                )
                returnUserAdviseDetailResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun returnSettingsResponse(settingsResponse: UserAdviseResponse) {
        withContext(Dispatchers.Main) {
            mutableSettingsResponse.value = settingsResponse
        }
    }

    private suspend fun returnNotificationResponse(settingsResponse: NotificationResponse) {
        withContext(Dispatchers.Main) {
            mutableNotificationResponse.value = settingsResponse
        }
    }

    private suspend fun returnCommonResponse(settingsResponse: CommonResponse) {
        withContext(Dispatchers.Main) {
            commonResponse.value = settingsResponse
        }
    }

    private suspend fun returnUserAdviseDetailResponse(userAdviseDetailResponse: UserAdviseDetailResponse) {
        withContext(Dispatchers.Main) {
            mutableUserAdviseDetailResponse.value = userAdviseDetailResponse
        }
    }

    fun userAdviseList(): LiveData<UserAdviseResponse> {
        return mutableSettingsResponse
    }

    fun userAdviseDetail(): LiveData<UserAdviseDetailResponse> {
        return mutableUserAdviseDetailResponse
    }

    fun addQuestionResponse(): LiveData<CommonResponse> {
        return commonResponse
    }
    fun notificationResponse(): LiveData<NotificationResponse> {
        return mutableNotificationResponse
    }
}