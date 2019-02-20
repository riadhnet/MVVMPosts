package test.riadh.mvvmposts.app

import android.app.Application
import org.koin.android.ext.android.startKoin
import test.riadh.mvvmposts.injection.DependencyModules

class MyApp : Application() {



    override fun onCreate() {
        super.onCreate()
        // start Koin context
        startKoin(
            this, listOf(
                DependencyModules.appModules
            )
        )
    }

}