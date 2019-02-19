package test.riadh.mvvmposts

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
import io.reactivex.Observable
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import test.riadh.mvvmposts.app.MyApp
import test.riadh.mvvmposts.model.Post
import test.riadh.mvvmposts.model.PostDao
import test.riadh.mvvmposts.model.PostRepositoryImpl
import test.riadh.mvvmposts.ui.post.PostListViewModel
import test.riadh.mvvmposts.utils.ExceptionUtil


class PostListViewModelTest {

    @get:Rule
    val schedulers = RxImmediateSchedulerRule()

    @MockK
    lateinit var postApi: PostRepositoryImpl

    lateinit var postDao: PostDao

    @MockK
    lateinit var myApp: MyApp

    lateinit var exceptionUtil: ExceptionUtil

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
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        postDao = PostDaoImpl()
        // mockkStatic(ExceptionUtil.Companion::class)
        // mockkStatic(ExceptionUtil::class)
        mockkObject(ExceptionUtil)
        every { ExceptionUtil.showError(any()) } answers { "Exception" }
//        mockkStatic(MyApp::class)
//        every { MyApp.instance } returns MyApp()
        every { postApi.getPosts() } returns Observable.just(postList)

    }


    @Test
    fun showDataFromApi() {
        val postListViewModel = PostListViewModel(postDao, postApi)
        assertEquals("verify that post was saved in data base ", postList.size, postDao.all.size)
//        assertEquals(
//            "Check that adapter has correct number of rows ",
//            postList.size,
//            postListViewModel.postListAdapter.itemCount
//        )

    }

    private class PostDaoImpl : PostDao {
        var posts = mutableListOf<Post>()

        override val all: List<Post>
            get() = posts

        override fun insertAll(vararg posts: Post) {
            this.posts.addAll(posts)
        }
    }

//    private class ExceptionUtilImpl : ExceptionUtil {
//
//    }

}


