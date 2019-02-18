package test.riadh.mvvmposts.injection.module

import dagger.Binds
import dagger.Module
import dagger.android.support.AndroidSupportInjectionModule
import test.riadh.mvvmposts.MyApp
import javax.inject.Qualifier

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class AppContext

@Module(includes = arrayOf(AndroidSupportInjectionModule::class))
abstract class ApplicationModule(

) {

    @Binds
    @AppContext
    abstract fun bindMyApp(myApp: MyApp): MyApp

}