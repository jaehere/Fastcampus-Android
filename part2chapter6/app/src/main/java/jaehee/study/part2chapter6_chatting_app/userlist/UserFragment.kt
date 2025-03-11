package jaehee.study.part2chapter6_chatting_app.userlist

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import jaehee.study.part2chapter6_chatting_app.Key.Companion.DB_CHAT_ROOMS
import jaehee.study.part2chapter6_chatting_app.Key.Companion.DB_USERS
import jaehee.study.part2chapter6_chatting_app.R
import jaehee.study.part2chapter6_chatting_app.chatdetail.ChatActivity
import jaehee.study.part2chapter6_chatting_app.chatlist.ChatRoomItem
import jaehee.study.part2chapter6_chatting_app.databinding.FragmentUserlistBinding
import java.util.UUID

class UserFragment : Fragment(R.layout.fragment_userlist) {

    private lateinit var binding: FragmentUserlistBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserlistBinding.bind(view)

        val userListAdapter = UserAdapter { otherUser ->
            val myUserId = Firebase.auth.currentUser?.uid ?: ""
            val chatRoomDB = Firebase.database.reference.child(DB_CHAT_ROOMS)
                .child(myUserId).child(otherUser.userId ?: "")

            chatRoomDB.get().addOnSuccessListener {
                var chatRoomId = ""
                if(it.value != null) {
                    // 데이터가 존재
                    val chatRoom = it.getValue(ChatRoomItem::class.java)
                    chatRoomId = chatRoom?.chatRoomId ?: ""
                } else {
                    chatRoomId = UUID.randomUUID().toString()
                    val newChatRoom = ChatRoomItem(
                        chatRoomId = chatRoomId,
                        otherUserName = otherUser.username,
                        otherUserId = otherUser.userId,
                    )
                    chatRoomDB.setValue(newChatRoom)
                }

                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra(ChatActivity.EXTRA_OTHER_USER_ID, otherUser.userId)
                intent.putExtra(ChatActivity.EXTRA_CHAT_ROOM_ID, chatRoomId)

                startActivity(intent)
            }
        }

        binding.userListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userListAdapter

        }

        val currentUserId = Firebase.auth.currentUser?.uid ?: ""

        Firebase.database.reference.child(DB_USERS)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userItemList = mutableListOf<UserItem>()

                    snapshot.children.forEach() {
                        val user = it.getValue(UserItem::class.java)
                        user ?: return
                        if(user.userId != currentUserId){
                            userItemList.add(user)
                        }
                    }

                    userListAdapter.submitList(userItemList)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

//        userListAdapter.submitList(
//            mutableListOf<UserItem?>().apply {
//                add(UserItem("11", "22", "33"))
//            }
//        )
    }
}