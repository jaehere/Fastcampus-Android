package jaehee.study.part2chapter6_chatting_app.userlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import jaehee.study.part2chapter6_chatting_app.R
import jaehee.study.part2chapter6_chatting_app.databinding.FragmentUserlistBinding

class UserFragment : Fragment(R.layout.fragment_userlist) {

    private lateinit var binding: FragmentUserlistBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserlistBinding.bind(view)

        val userListAdapter = UserAdapter()
        binding.userListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userListAdapter

        }
        userListAdapter.submitList(
            mutableListOf<UserItem?>().apply {
                add(UserItem("11", "22", "33"))
            }
        )
    }
}