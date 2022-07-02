package ty.learns.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ty.learns.android.ndk.HeyFFmpeg
import ty.learns.android.ndk.HeyNDK
import ty.learns.android.utilities.INTENT_HEY_WORLD_EXTRA

class MainActivity : AppCompatActivity() {

    lateinit var robotView: ImageView
    lateinit var editView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        robotView = findViewById(R.id.imageView3)

        robotView.setOnClickListener {
            Toast.makeText(this, "Hey, Android!", Toast.LENGTH_LONG).show()
        }

        editView = findViewById(R.id.editTextTextHey)
        editView.requestFocus() // 获得焦点

        // Show the keyboard.
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editView, 0)

        // NDK
        findViewById<TextView>(R.id.act_main_ndk_textView).text = getHeyStringFromJNI("Android")

        // runNDK
        runNDK()
    }

    fun sendMessage(view: View) {
        // Hide the keyboard.
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

        val editView = findViewById<EditText>(R.id.editTextTextHey)
        val message = editView.text.toString()
        val intent = Intent(this, HomeActivity::class.java).apply {
            putExtra(INTENT_HEY_WORLD_EXTRA, message)
        }
        startActivity(intent)
    }

    private external fun getHeyStringFromJNI(name: String): String

    companion object {
        init {
            System.loadLibrary("hey_ndk")
        }
    }

    // 运行所有小代码的地方
    private fun runNDK() {
        //runHeyNDKJava()

        HeyFFmpeg.sayHey()
    }

    private fun runHeyNDKJava() {
        HeyNDK.printHeyWords()
    }
}