package com.example.familyconnectv2.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.familyconnectv2.R
import com.example.familyconnectv2.models.userResponse
import com.example.familyconnectv2.sp.SharedViewModel

class UsersAdapter(private val onUserClickListener: (String) -> Unit) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
    private var userList: List<userResponse> = emptyList()
    private val selectedUsers: HashSet<String> = HashSet()
    private val selectedUsersemail: HashSet<String> = HashSet()
    private val selectedUserIdeas: MutableList<String> = mutableListOf()
    private var currentUserEmail: String = ""

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageAvatar: ImageView = itemView.findViewById(R.id.imageAvatar)
        val textUsername: TextView = itemView.findViewById(R.id.textUsername)
        val textEmail: TextView = itemView.findViewById(R.id.textEmail)
    }

    fun updateUsers(newUsers: List<userResponse>) {
        userList = newUsers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.allusersrecycleitem, parent, false)
        return UserViewHolder(view)
    }

    fun setCurrentUserEmail(email: String) {
        currentUserEmail = email
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]




        holder.textUsername.text = user.name
        holder.textEmail.text = user.email
        holder.itemView.setOnClickListener {
            val username = user.name
            val email = user.email
            // Provera da li je korisnik već izabran
            if (!selectedUsers.contains(username) && currentUserEmail!=user.email ) {
                // Dodajemo korisnika u Set izabranih
                selectedUsers.add(username)
                selectedUserIdeas.addAll(listOf(user._id))
                Log.d("UsersAdapter", "Selected user ideas: $selectedUserIdeas")

                // Pozivamo funkciju onUserClickListener i prosleđujemo ime korisnika
                onUserClickListener.invoke(getSelectedUsersText())
            }else
            {
                Toast.makeText(holder.itemView.context, "Ne možete izabrati samog sebe", Toast.LENGTH_SHORT).show()
            }




    }}

    override fun getItemCount(): Int {
        return userList.size
    }

    private fun getSelectedUsersText(): String {
        // Konvertujemo HashSet u formatiran niz stringova sa zarezima
        return selectedUsers.joinToString(", ")
    }

    fun getSelectedUsersEmails(): List<String> {
        return selectedUsersemail.toList()
    }

    fun getSelectedUserIdeas(): List<String> {
        return selectedUserIdeas
    }
}

