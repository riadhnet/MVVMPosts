package test.riadh.mvvmposts.network

import io.reactivex.Observable
import retrofit2.http.GET
import test.riadh.mvvmposts.model.Post

/**
 * The interface which provides methods to get result of webservices
 */
interface PostApi {
    /**
     * Get the list of the pots from the API
     */
    @GET("/posts")
    fun getPosts(): Observable<List<Post>>
}