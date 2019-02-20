package test.riadh.mvvmposts

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import test.riadh.mvvmposts.model.Post
import test.riadh.mvvmposts.model.PostDao
import test.riadh.mvvmposts.model.PostRepositoryImpl
import test.riadh.mvvmposts.ui.post.PostListViewModel
import test.riadh.mvvmposts.utils.ExceptionUtilInterface


class PostListViewModelTest {

    @get:Rule
    val schedulers = RxImmediateSchedulerRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var postApi: PostRepositoryImpl

    lateinit var postDao: PostDao

    @MockK
    lateinit var exceptionUtil: ExceptionUtilInterface


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

        every { exceptionUtil.showError(any()) } returns "Error"

        every { postApi.getPosts() } returns Observable.just(postList)

    }


    @Test
    fun showDataFromApi() {
        //GIVEN
        postDao = PostDaoImpl()
        val postListViewModel = PostListViewModel(postDao, postApi, exceptionUtil)
        //WHEN
        postListViewModel.loadPosts()
        //THEN
        assertEquals("verify that post was saved in data base ", postList.size, postDao.all.size)
        assertEquals(
            "Check that adapter has correct number of rows ",
            postList.size,
            postListViewModel.postListAdapter.itemCount
        )

    }

    @Test
    fun showError() {
        //GIVEN
        postDao = PostDaoImpl()
        val x = Exception()
        every { postApi.getPosts() } returns Observable.error(x)
        val postListViewModel = spyk(PostListViewModel(postDao, postApi, exceptionUtil), recordPrivateCalls = true)
        //WHEN
        postListViewModel.loadPosts()
        //THEN
        verify { postListViewModel invoke "onRetrievePostListError" withArguments listOf(x) }


    }


    private class PostDaoImpl : PostDao {
        var posts = mutableListOf<Post>()

        override val all: List<Post>
            get() = posts

        override fun insertAll(vararg posts: Post) {
            this.posts.addAll(posts)
        }
    }

}


