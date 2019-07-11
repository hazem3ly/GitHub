package com.moduscapital.github.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.moduscapital.github.data.network.response.RepoDetails

@Database(
        entities = [RepoDetails::class],
        version = 1
)
abstract class ReposDatabase : RoomDatabase() {

    abstract fun repoDetailsDao(): RepoDetailsDao

    companion object {
        @Volatile
        private var inistance: ReposDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = inistance ?: synchronized(LOCK) {
            inistance ?: buildDatabase(context).also { inistance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext,
                ReposDatabase::class.java,
                "repos.db"
        ).build()
    }

}