package ty.learns.android.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FirstDAO {
    @Insert
    suspend fun insert(first: First)

    @Update
    suspend fun update(first: First)

    @Query("SELECT * from first_table WHERE id = :key")
    suspend fun get(key: Long): First?

    @Query("DELETE FROM first_table")
    suspend fun clear()

    @Query("SELECT * FROM first_table ORDER BY id DESC LIMIT 1")
    suspend fun getOne(): First?

    @Query("SELECT * FROM first_table ORDER BY id DESC")
    fun getAll(): LiveData<List<First>>

}