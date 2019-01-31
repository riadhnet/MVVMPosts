package test.riadh.mvvmposts.ui.post

import android.view.View
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import test.riadh.mvvmposts.base.BaseViewModel
import test.riadh.mvvmposts.model.Post
import test.riadh.mvvmposts.network.PostApi
import test.riadh.mvvmposts.utils.ExceptionUtil
import javax.inject.Inject

class PostListViewModel : BaseViewModel() {
    @Inject
    lateinit var postApi: PostApi

    private lateinit var subscription: Disposable
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

    val postListAdapter: PostListAdapter = PostListAdapter()

    val errorClickListener = View.OnClickListener { loadPosts() }

    init {
        loadPosts()
    }


    private fun loadPosts() {
        subscription = postApi.getPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrievePostListStart() }
            .doOnTerminate { onRetrievePostListFinish() }
            .subscribe(
                { result -> onRetrievePostListSuccess(result) },
                { error -> onRetrievePostListError(error) }
            )


    }


    private fun onRetrievePostListStart() {
        errorMessage.value = null
        loadingVisibility.value = View.VISIBLE
    }

    private fun onRetrievePostListFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrievePostListSuccess(result: List<Post>) {
        postListAdapter.updatePostList(result)
    }

    private fun onRetrievePostListError(error: Throwable) {
        //errorMessage.value = R.string.post_error
        errorMessage.value = ExceptionUtil.showError(error)
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

}