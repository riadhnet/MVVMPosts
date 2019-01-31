package test.riadh.mvvmposts.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import test.riadh.mvvmposts.model.Post
import test.riadh.mvvmposts.model.PostDao

@Database(entities = [Post::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}