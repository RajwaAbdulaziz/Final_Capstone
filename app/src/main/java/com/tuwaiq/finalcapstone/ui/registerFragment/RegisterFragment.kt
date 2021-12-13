package com.tuwaiq.finalcapstone.ui.registerFragment

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.tuwaiq.finalcapstone.R
import com.tuwaiq.finalcapstone.ui.calenderFragment.CalenderFragment
import com.tuwaiq.finalcapstone.ui.loginFragment.LoginFragment

class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var nameEt: EditText
    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var confirmPasswordEt: EditText
    private lateinit var registerBtn: Button

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

        val currentUser = auth.currentUser

        if (currentUser != null) {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

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
                else -> register(email, pass)
            }
        }
    }

    private fun register(email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast("Successful register")
                    val loginFragment = LoginFragment()
                    activity?.supportFragmentManager
                        ?.beginTransaction()?.replace(R.id.fragmentContainerView, loginFragment)?.commit()
                } else {
                    showToast(it.result.toString())
                }
            }
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}