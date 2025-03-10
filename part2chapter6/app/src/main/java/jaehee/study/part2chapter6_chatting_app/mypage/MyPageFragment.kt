package jaehee.study.part2chapter6_chatting_app.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import jaehee.study.part2chapter6_chatting_app.LoginActivity
import jaehee.study.part2chapter6_chatting_app.R
import jaehee.study.part2chapter6_chatting_app.databinding.FragmentMyPageBinding

class MyPageFragment : Fragment(R.layout.fragment_my_page) {

    private lateinit var binding: FragmentMyPageBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyPageBinding.bind(view)

        binding.applyButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()

            if (username.isEmpty()) {
                Toast.makeText(context, "유저이름은 빈값일 수 없습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //todo 파이어베이스 realtime database update
        }

        binding.signOutButton.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }
    }
}