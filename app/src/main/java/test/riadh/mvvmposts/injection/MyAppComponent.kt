package test.riadh.mvvmposts.injection


import android.content.Context
import dagger.BindsInstance
import dagger.Component
import test.riadh.mvvmposts.injection.module.DataModule
import test.riadh.mvvmposts.utils.ExceptionUtilInterface
import javax.inject.Singleton


@Singleton
@Component(modules = [DataModule::class])
interface MyAppComponent {

    fun inject(exceptionUtilInterface: ExceptionUtilInterface)

    @Component.Builder
    interface Builder {


        @BindsInstance
        fun context(context: Context): Builder

        fun build(): MyAppComponent


    }

}

