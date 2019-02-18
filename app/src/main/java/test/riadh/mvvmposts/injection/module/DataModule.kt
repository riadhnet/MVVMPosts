package test.riadh.mvvmposts.injection.module

import dagger.Module
import dagger.Provides
import test.riadh.mvvmposts.utils.ExceptionUtil
import javax.inject.Singleton


@Module
class DataModule(

) {

    @Provides
    @Singleton
    fun providesExceptionUtil(): ExceptionUtil {
        return ExceptionUtil()
    }
}