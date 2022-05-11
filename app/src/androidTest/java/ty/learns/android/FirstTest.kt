package ty.learns.android

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ty.learns.android.database.First
import ty.learns.android.database.FirstDAO
import ty.learns.android.database.FirstDB
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class FirstTest {
    private lateinit var dao: FirstDAO
    private lateinit var db: FirstDB

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, FirstDB::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        dao = db.firstDAO
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    suspend fun insertAndGetNight() {
        val night = First(0, "Hey, Android!", 0)
        dao.insert(night)
        val first = dao.getOne()
        assertEquals(first?.inputText, "Hey, Android!")
    }
}