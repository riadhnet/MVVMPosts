package test.riadh.mvvmposts.utils


import android.content.Context
import java.lang.ref.WeakReference

class ResourceProvider(private val context: WeakReference<Context>) {

    fun getString(resId: Int): String {
        return context.get()!!.getString(resId)
    }

    fun getString(resId: Int, value: String): String {
        return context.get()!!.getString(resId, value)
    }

}
