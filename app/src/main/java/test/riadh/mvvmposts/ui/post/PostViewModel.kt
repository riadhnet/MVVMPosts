package test.riadh.mvvmposts.ui.post

import androidx.lifecycle.MutableLiveData
import test.riadh.mvvmposts.base.BaseViewModel
import test.riadh.mvvmposts.model.Post

class PostViewModel: BaseViewModel() {
    private val postTitle = MutableLiveData<String>()
    private val postBody = MutableLiveData<String>()

    fun bind(post: Post){
        postTitle.value = post.title
        postBody.value = post.body
    }

    fun getPostTitle():MutableLiveData<String>{
        return postTitle
    }

    fun getPostBody():MutableLiveData<String>{
        return postBody
    }
}