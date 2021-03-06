package test.riadh.mvvmposts.utils

import okhttp3.ResponseBody
import retrofit2.HttpException
import test.riadh.mvvmposts.R
import test.riadh.mvvmposts.app.MyApp
import java.net.ConnectException


class ExceptionUtil {


    companion object {

        fun showError(throwable: Throwable): String {

            if (throwable is HttpException) {
                when (throwable.code()) {
                    404 -> return MyApp.instance.getResourceProvider().getString(R.string.not_found)
                    401 -> return MyApp.instance.getResourceProvider().getString(R.string.not_authorized)
                    else -> handleApiError(throwable.response().errorBody()!!)
                }
            } else if (throwable is ConnectException) {
                return MyApp.instance.getResourceProvider().getString(R.string.check_internet)
            } else if (throwable is SecurityException) {
                return throwable.localizedMessage
            }

            return MyApp.instance.getResourceProvider().getString(R.string.post_error)
        }


        private fun handleApiError(responseBody: ResponseBody): String {
            return responseBody.string()
        }


    }


}