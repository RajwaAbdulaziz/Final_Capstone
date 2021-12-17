package com.tuwaiq.finalcapstone.ui.loginFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tuwaiq.finalcapstone.R
import com.tuwaiq.finalcapstone.model.User
import com.tuwaiq.finalcapstone.ui.calenderFragment.CalenderFragment
import com.tuwaiq.finalcapstone.ui.registerFragment.RegisterFragment

class LoginFragment : Fragment() {

    private lateinit var loginBtn: Button
    private lateinit var registerTv: TextView
    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText

    private val loginViewModel by lazy { ViewModelProvider(this)[LoginViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.login_fragment, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        loginBtn = view.findViewById(R.id.login_btn)
        registerTv = view.findViewById(R.id.login_to_register_tv)
        emailEt = view.findViewById(R.id.login_email_et)
        passwordEt = view.findViewById(R.id.login_password_et)
    }

    override fun onStart() {
        super.onStart()

        if (loginViewModel.currentUser != null) {
            findNavController().navigate(R.id.action_loginFragment_to_calenderFragment2)
        }

        loginBtn.setOnClickListener {

            val email = emailEt.text.toString()
            val password = passwordEt.text.toString()

            when{
                email.isEmpty() -> showToast("Enter your email")
                password.isEmpty() -> showToast("Enter your password")
                else -> {
                    loginViewModel.login(email, password)
                    findNavController().navigate(R.id.action_loginFragment_to_calenderFragment2)
                }
            }
        }

        registerTv.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}