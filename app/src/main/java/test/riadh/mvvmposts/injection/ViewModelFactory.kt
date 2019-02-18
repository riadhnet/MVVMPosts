package test.riadh.mvvmposts.injection

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import test.riadh.mvvmposts.injection.module.retrofitPostApi
import test.riadh.mvvmposts.model.database.AppDatabase
import test.riadh.mvvmposts.ui.post.PostListViewModel

class ViewModelFactory(private val activity: AppCompatActivity): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostListViewModel::class.java)) {
            val db = Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java, "posts").build()
            @Suppress("UNCHECKED_CAST")
            return PostListViewModel(db.postDao(), retrofitPostApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}