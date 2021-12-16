package com.tuwaiq.finalcapstone.ui.registerFragment

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.tuwaiq.finalcapstone.R
import com.tuwaiq.finalcapstone.model.User
import com.tuwaiq.finalcapstone.ui.calenderFragment.CalenderFragment
import com.tuwaiq.finalcapstone.ui.loginFragment.LoginFragment
import com.tuwaiq.finalcapstone.ui.moodFragment.MoodFragmentDirections
import com.tuwaiq.finalcapstone.utils.FirebaseUtils

private const val TAG = "RegisterFragment"
class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var nameEt: EditText
    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var confirmPasswordEt: EditText
    private lateinit var registerBtn: Button
    private var userId: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.register_fragment, container, false)
        nameEt = view.findViewById(R.id.register_name_et)
        emailEt = view.findViewById(R.id.register_email_et)
        passwordEt = view.findViewById(R.id.register_password_et)
        confirmPasswordEt = view.findViewById(R.id.register_conf_pass_et)
        registerBtn = view.findViewById(R.id.register_btn)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

//        val currentUser = auth.currentUser
//
//        userId = currentUser?.uid
//
//        if (currentUser != null) {
//            //findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
//        }

        registerBtn.setOnClickListener {
            val name = nameEt.text.toString()
            val email = emailEt.text.toString()
            val pass = passwordEt.text.toString()
            val confPass = confirmPasswordEt.text.toString()

            when{
                name.isEmpty() -> showToast("Please enter a name")
                email.isEmpty() -> showToast("Please enter an email")
                pass.isEmpty() -> showToast("Please enter a password")
                confPass != pass -> showToast("Please enter a matching password")
                else -> register(name, email, pass)
            }
        }
    }

    private fun register(name: String, email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    val user = auth.currentUser

                    val users = User(name, email)

                    Log.d(TAG, "email: ${users.email}")

                    val db = FirebaseFirestore.getInstance()
                        db.collection("Users")
                            .document(user!!.uid).set(users)

                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                } else {
                    Log.d(TAG, "problem occurred")
                    showToast(it.result.toString())
                }
            }
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}