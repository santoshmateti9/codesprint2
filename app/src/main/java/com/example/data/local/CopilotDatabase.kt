package com.example.data.local

import android.content.Context
import androidx.room.*
import com.example.data.model.CopilotStateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CopilotDao {
    @Query("SELECT * FROM copilot_state WHERE id = 1 LIMIT 1")
    fun getCopilotState(): Flow<CopilotStateEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCopilotState(state: CopilotStateEntity)

    @Query("DELETE FROM copilot_state")
    suspend fun clearState()
}

@Database(entities = [CopilotStateEntity::class], version = 1, exportSchema = false)
abstract class CopilotDatabase : RoomDatabase() {
    abstract fun copilotDao(): CopilotDao

    companion object {
        @Volatile
        private var INSTANCE: CopilotDatabase? = null

        fun getDatabase(context: Context): CopilotDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CopilotDatabase::class.java,
                    "copilot_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
