package test.riadh.mvvmposts.model

import io.reactivex.Observable
import test.riadh.mvvmposts.network.PostApi


class PostRepositoryImpl(private val postApi: PostApi) {
    fun getPosts(): Observable<List<Post>> {
        return postApi.getPosts()
    }
}