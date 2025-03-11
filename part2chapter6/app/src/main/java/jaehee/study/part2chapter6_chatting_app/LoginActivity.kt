package jaehee.study.part2chapter6_chatting_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import jaehee.study.part2chapter6_chatting_app.Key.Companion.DB_USERS
import jaehee.study.part2chapter6_chatting_app.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("LoginActivity", "onCreate")
        //회원가입
        binding.signUpButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) { // 예외처리
                Toast.makeText(this, "이메일 또는 패스워드가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Firebase.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // 회원가입 성공

                        Toast.makeText(this, "회원가입에 성공했습니다. 로그인해주세요", Toast.LENGTH_SHORT).show()
                    } else {
                        // 회원가입 실패
                        Log.e("LoginActivity", "회원가입 실패 원인: ${task.exception.toString()}")
                        Toast.makeText(this, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        //로그인
        binding.signInButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) { // 예외처리
                Toast.makeText(this, "이메일 또는 패스워드가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    val currentUSer = Firebase.auth.currentUser
                    if (task.isSuccessful && currentUSer != null) {
                        val userId = currentUSer.uid

                        Firebase.messaging.token.addOnCompleteListener {
                            val token = it.result

                            val user = mutableMapOf<String, Any>()
                            user["userId"] = userId
                            user["username"] = email
                            user["fcmToken"] = token

                            Firebase.database.reference.child(DB_USERS).child(userId)
                                .updateChildren(user)
                            // 로그인 성공
                            Toast.makeText(this, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }.addOnFailureListener {
                            //실패 처리
                        }
                    } else {
                        // 로그인 실패
                        Log.e("LoginActivity", "로그인 실패 원인: ${task.exception.toString()}")
                        Toast.makeText(this, "로그인에 실패했습니다. ${task.exception}", Toast.LENGTH_SHORT).show()
                    }

                }
        }


    }
}