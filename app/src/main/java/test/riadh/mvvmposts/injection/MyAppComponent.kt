package test.riadh.mvvmposts.injection


import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import test.riadh.mvvmposts.MyApp
import test.riadh.mvvmposts.injection.module.ApplicationModule
import test.riadh.mvvmposts.injection.module.DataModule
import javax.inject.Singleton


@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, ApplicationModule::class, DataModule::class])
interface MyAppComponent : AndroidInjector<MyApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun app(myApp: MyApp): Builder

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): MyAppComponent
    }

}

