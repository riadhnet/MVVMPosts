package test.riadh.mvvmposts.base

import androidx.lifecycle.ViewModel
import test.riadh.mvvmposts.injection.component.DaggerViewModelInjector
import test.riadh.mvvmposts.injection.component.ViewModelInjector
import test.riadh.mvvmposts.injection.module.NetworkModule
import test.riadh.mvvmposts.ui.post.PostListViewModel


abstract class BaseViewModel: ViewModel(){

    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }


    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is PostListViewModel -> injector.inject(this)
        }
    }
}