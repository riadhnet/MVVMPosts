package test.riadh.mvvmposts

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import test.riadh.mvvmposts.model.Post
import test.riadh.mvvmposts.model.PostDao
import test.riadh.mvvmposts.network.PostApi
import test.riadh.mvvmposts.ui.post.PostListViewModel
import test.riadh.mvvmposts.utils.ExceptionUtil
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class PostListViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var postApi: PostApi

    lateinit var postDao: PostDao

    @Mock
    lateinit var exceptionUtil: ExceptionUtil


    private val post1 =
        Post(1, 1, "sunt aut facere repellat provident occaecati excepturi", "recusandae consequuntur expedita")
    private val post2 =
        Post(2, 2, "sunt aut facere repellat provident occaecati excepturi", "recusandae consequuntur expedita")
    private val post3 =
        Post(3, 3, "sunt aut facere repellat provident occaecati excepturi", "recusandae consequuntur expedita")
    private val post4 =
        Post(4, 4, "sunt aut facere repellat provident occaecati excepturi", "recusandae consequuntur expedita")

    private val postList = listOf(post1, post2, post3, post4)


    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        postDao = PostDaoImpl()
        Mockito.`when`(postApi.getPosts()).thenReturn(Observable.fromArray(postList))

        val immediate = object : Scheduler() {
            override fun scheduleDirect(@NonNull run: Runnable, delay: Long, @NonNull unit: TimeUnit): Disposable {
                // this prevents StackOverflowErrors when scheduling with a delay
                return super.scheduleDirect(run, 0, unit)
            }

            override fun createWorker(): Scheduler.Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, false)
            }
        }

        RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }

    }

    @Test
    fun showDataFromApi() {

        val postListViewModel = PostListViewModel(postDao, postApi, exceptionUtil)
        assertEquals("verify that post was saved in data base ", postList.size, postDao.all.size)
        assertEquals(
            "Check that adapter has correct number of rows ",
            postList.size,
            postListViewModel.postListAdapter.itemCount
        )

    }

}

private class PostDaoImpl : PostDao {
    var posts = mutableListOf<Post>()

    override val all: List<Post>
        get() = posts

    override fun insertAll(vararg posts: Post) {
        this.posts.addAll(posts)
    }

    override fun deleteAll() {
        this.posts.clear()
    }

}