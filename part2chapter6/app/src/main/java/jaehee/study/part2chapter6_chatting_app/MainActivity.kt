package jaehee.study.part2chapter6_chatting_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MainActivity", "onCreate")
        val currentUser = Firebase.auth.currentUser

        if(currentUser == null) {
            //로그인이 안되어있음
            startActivity(Intent(this, LoginActivity::class.java))
            finish()

        }

    }
}