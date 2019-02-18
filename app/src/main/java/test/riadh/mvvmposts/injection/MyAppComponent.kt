package test.riadh.mvvmposts.injection


import android.content.Context
import dagger.BindsInstance
import dagger.Component
import test.riadh.mvvmposts.injection.module.ApplicationModule
import test.riadh.mvvmposts.injection.module.DataModule
import test.riadh.mvvmposts.utils.ExceptionUtil
import javax.inject.Singleton


@Singleton
@Component(modules = [ApplicationModule::class, DataModule::class])
interface MyAppComponent {

    fun inject(exceptionUtil: ExceptionUtil)

    @Component.Builder
    interface Builder {


        @BindsInstance
        fun context(context: Context): Builder

        fun build(): MyAppComponent


    }

}

