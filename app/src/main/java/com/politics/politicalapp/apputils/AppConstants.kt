package app.app.patidarsaurabh.apputils

interface AppConstants {

    companion object {
        const val ACTION_CODE_100 = 100
        const val IS_LOGIN: String = "IS_LOGIN"
        const val MOBILE: String = "MOBILE"
        const val GID: String = "GID"
        const val SETTINGS: String = "SETTINGS"
        const val MOBILE_VERIFIED: String = "MOBILE_VERIFIED"
        const val NEWS_ID: String = "NEWS_ID"
        const val IMAGE_LIST: String = "IMAGE_LIST"
        const val IMAGE_URL: String = "IMAGE_URL"
        const val VATAN_GHAM_ID: String = "VATAN_GHAM_ID"
        const val PARIVAR_DARSHAN_ID: String = "PARIVAR_DARSHAN_ID"
        const val UMIYA_MANDIR_ID: String = "UMIYA_MANDIR_ID"
        const val SAMAJIK_SANSTHA_ID: String = "SAMAJIK_SANSTHA_ID"
        const val PATIDAR_SAMAJ_ID: String = "PATIDAR_SAMAJ_ID"
        const val IMAGE_POSITION: String = "IMAGE_POSITION"
        const val FILE_NAME: String = "FILE_NAME"
        const val NAME: String = "NAME"
        const val ORDER_ID: String = "ORDER_ID"
        const val AMOUNT: String = "AMOUNT"
        const val ADD_LAVAJAM: String = "ADD_LAVAJAM"
        const val SHOW_SUBMIT: String = "SHOW_SUBMIT"
        const val ID: String = "id"
    }

    interface APIEndPoints {
        companion object {
            //const val BASE_URL = "https://www.patidarsaurabh.com/demo/patidarsaurabhapi/"
            const val BASE_URL = "https://colorsofgujarat.in/cogapp/cogapi/"
            const val REGISTRATION: String = "registration"
            const val GET_SETTINGS: String = "get_settings"
            const val GET_GOVT_WORK: String = "get_gov_work"
            const val GET_GOVT_WORK_DETAIL: String = "get_govwork_detail"
            const val ADD_GOVT_WORK_RATING: String = "add_govwork_user_rating"
        }
    }

    interface RequestParameters {
        companion object {
            const val USER_MOBILE = "user_mobile"
            const val district_id = "district_id"
            const val start = "start"
            const val end = "end"
            const val city = "city"
            const val platform = "platform"
            const val name = "name"
            const val mobile = "mobile"
            const val gid = "gid"
            const val user_mobile = "user_mobile"
            const val user_rating = "user_rating"
            const val CITY_ID = "city_id"
        }
    }
}