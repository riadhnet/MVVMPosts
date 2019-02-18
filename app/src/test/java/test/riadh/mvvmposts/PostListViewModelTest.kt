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
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import test.riadh.mvvmposts.model.Post
import test.riadh.mvvmposts.model.PostDao
import test.riadh.mvvmposts.network.PostApi
import test.riadh.mvvmposts.ui.post.PostListViewModel
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

@RunWith(
    RobolectricTestRunner::class
)
class PostListViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var postApi: PostApi

    //@Mock
    lateinit var postDao: PostDao

    @Mock
    lateinit var myApp: MyApp


    val post1 =
        Post(1, 1, "sunt aut facere repellat provident occaecati excepturi", "recusandae consequuntur expedita")
    val post2 =
        Post(2, 2, "sunt aut facere repellat provident occaecati excepturi", "recusandae consequuntur expedita")
    val post3 =
        Post(3, 3, "sunt aut facere repellat provident occaecati excepturi", "recusandae consequuntur expedita")
    val post4 =
        Post(4, 4, "sunt aut facere repellat provident occaecati excepturi", "recusandae consequuntur expedita")

    val postList = listOf(post1, post2, post3, post4)


    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        myApp = RuntimeEnvironment.application as MyApp
        postDao = PostDaoImpl()
        Mockito.`when`(postApi.getPosts()).thenReturn(Observable.fromArray(postList))

        //TODO fix me
        //Mockito.`when`(MyApp.instance).thenReturn(MyApp())

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
        postDao.deleteAll()

        val postListViewModel = PostListViewModel(postDao, postApi)
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