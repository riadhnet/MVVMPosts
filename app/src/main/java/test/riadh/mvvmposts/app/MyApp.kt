package test.riadh.mvvmposts.app

import android.app.Application
import org.koin.android.ext.android.startKoin
import test.riadh.mvvmposts.injection.DependencyModules
import test.riadh.mvvmposts.utils.ResourceProvider
import java.lang.ref.WeakReference

class MyApp : Application() {

    private var mResourceProvider: ResourceProvider? = null

    companion object {
        lateinit var instance: MyApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // start Koin context
        startKoin(this, listOf(
           DependencyModules.appModules
        ))


    }

    fun getResourceProvider(): ResourceProvider {
        if (mResourceProvider == null)
            mResourceProvider = ResourceProvider(WeakReference(this))

        return mResourceProvider!!
    }

}