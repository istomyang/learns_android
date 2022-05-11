package ty.learns.android.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "first_table")
data class First(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    @ColumnInfo(name = "input_text")
    var inputText: String,
    @ColumnInfo(name = "tap_id")
    var tapId: Int, ) {
//    var id: Long = 0L
//    var greet: String = "Hey, Android!"
//    var reply: String = "Hey! Tom"
//    var timeStampMillis: Long = System.currentTimeMillis()
}
