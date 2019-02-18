package test.riadh.mvvmposts

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import test.riadh.mvvmposts.injection.DaggerMyAppComponent
import test.riadh.mvvmposts.injection.MyAppComponent
import test.riadh.mvvmposts.utils.ResourceProvider
import java.lang.ref.WeakReference

class MyApp : DaggerApplication() {


    private var mResourceProvider: ResourceProvider? = null

    companion object {
        lateinit var instance: MyAppComponent
            private set
    }


    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerMyAppComponent.builder()
            .app(this)
            .context(this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
    }

    fun getResourceProvider(): ResourceProvider {
        if (mResourceProvider == null)
            mResourceProvider = ResourceProvider(WeakReference(this))

        return mResourceProvider!!
    }

}