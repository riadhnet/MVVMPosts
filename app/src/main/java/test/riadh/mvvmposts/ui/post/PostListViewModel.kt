package test.riadh.mvvmposts.ui.post

import android.view.View
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import test.riadh.mvvmposts.base.BaseViewModel
import test.riadh.mvvmposts.model.Post
import test.riadh.mvvmposts.model.PostDao
import test.riadh.mvvmposts.network.PostApi
import test.riadh.mvvmposts.utils.ExceptionUtil

class PostListViewModel(
    private val postDao: PostDao,
    private val postApi: PostApi,
    private val exceptionUtil: ExceptionUtil
) : BaseViewModel() {

    private lateinit var subscription: Disposable
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

    val postListAdapter: PostListAdapter = PostListAdapter()

    val errorClickListener = View.OnClickListener { loadPosts() }

    init {
        loadPosts()
    }

    private fun loadPosts() {

        subscription = Observable.fromCallable { postDao.all }
            .concatMap { dbPostList ->
                if (dbPostList.isEmpty()) {
                    postApi.getPosts().concatMap { apiPostList ->
                        postDao.insertAll(*apiPostList.toTypedArray())
                        Observable.just(apiPostList)
                    }
                } else Observable.just(dbPostList)
            }
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
        // errorMessage.value = myApp.getResourceProvider().getString(R.string.not_found)
        errorMessage.value = exceptionUtil.showError(error)
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

}