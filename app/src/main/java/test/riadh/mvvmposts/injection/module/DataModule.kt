package test.riadh.mvvmposts.injection.module

import dagger.Module
import dagger.Provides
import test.riadh.mvvmposts.utils.ExceptionUtil
import test.riadh.mvvmposts.utils.ExceptionUtilInterface
import javax.inject.Singleton


@Module
class DataModule(

) {
    @Provides
    @Singleton
    fun providesExceptionUtil(): ExceptionUtilInterface {
        return ExceptionUtil()
    }
}