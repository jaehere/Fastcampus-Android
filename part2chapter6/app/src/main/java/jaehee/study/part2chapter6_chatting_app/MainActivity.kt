package jaehee.study.part2chapter6_chatting_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import jaehee.study.part2chapter6_chatting_app.chatlist.ChatListFragment
import jaehee.study.part2chapter6_chatting_app.databinding.ActivityMainBinding
import jaehee.study.part2chapter6_chatting_app.userlist.UserFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userFragment = UserFragment()
    private val chatListFragment = ChatListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("MainActivity", "onCreate")
        val currentUser = Firebase.auth.currentUser

        if (currentUser == null) {
            //로그인이 안되어있음
            startActivity(Intent(this, LoginActivity::class.java))
            finish()

        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.userList -> {
                    replaceFragment(userFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.chatroomList -> {
                    replaceFragment(chatListFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.myPage -> {
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener false
                }
            }
        }
        replaceFragment(userFragment)

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.frameLayout, fragment)
                commit()
            }
    }
}