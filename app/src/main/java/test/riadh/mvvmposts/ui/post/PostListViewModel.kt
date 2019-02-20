package test.riadh.mvvmposts.ui.post

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import test.riadh.mvvmposts.model.Post
import test.riadh.mvvmposts.model.PostDao
import test.riadh.mvvmposts.model.PostRepositoryImpl
import test.riadh.mvvmposts.utils.ExceptionUtilInterface

class PostListViewModel(
    private val postDao: PostDao,
    private val postApi: PostRepositoryImpl,
    private val exceptionUtil: ExceptionUtilInterface
) : ViewModel() {

    private lateinit var subscription: Disposable
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

    val postListAdapter: PostListAdapter = PostListAdapter()

    val errorClickListener = View.OnClickListener { loadPosts() }


    fun loadPosts() {

        subscription = Observable.fromCallable { postDao.all }
            .concatMap { dbPostList ->
                if (dbPostList.isEmpty()) {
                    postApi.getPosts().concatMap { apiPostList ->
                        postDao.insertAll(*apiPostList.toTypedArray())
                        Observable.just(apiPostList)
                    }
                } else
                    Observable.just(dbPostList)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrievePostListStart() }
            .doOnTerminate { onRetrievePostListFinish() }
            .subscribe(
                { result -> onRetrievePostListSuccess(result) },
                { t ->
                    onRetrievePostListError(t)
                }
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
        errorMessage.value = exceptionUtil.showError(error)
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

}