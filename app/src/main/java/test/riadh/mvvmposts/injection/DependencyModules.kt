package test.riadh.mvvmposts.injection

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import test.riadh.mvvmposts.injection.module.createOkHttpClient
import test.riadh.mvvmposts.injection.module.provideRetrofitInterface
import test.riadh.mvvmposts.model.PostRepositoryImpl
import test.riadh.mvvmposts.model.database.AppDatabase
import test.riadh.mvvmposts.network.PostApi
import test.riadh.mvvmposts.ui.post.PostListViewModel
import test.riadh.mvvmposts.ui.post.PostViewModel
import test.riadh.mvvmposts.utils.BASE_URL
import test.riadh.mvvmposts.utils.ExceptionUtil
import test.riadh.mvvmposts.utils.ExceptionUtilInterface

object DependencyModules {

    val appModules = module {

        single { PostRepositoryImpl(get()) }

        // Room Database
        single {
            Room.databaseBuilder(androidApplication(), AppDatabase::class.java, "posts").build()
        }

        single { get<AppDatabase>().postDao() }

        single { ExceptionUtil(androidContext()) as ExceptionUtilInterface }

        viewModel { PostListViewModel(get(), get(), get()) }

        viewModel { PostViewModel() }

        // provided web components
        single { createOkHttpClient() }

        // Fill property
        single { provideRetrofitInterface<PostApi>(createOkHttpClient(), BASE_URL) }

    }
}