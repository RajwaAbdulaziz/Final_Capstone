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
import com.tuwaiq.finalcapstone.R
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
        loginBtn = view.findViewById(R.id.login_btn)
        registerTv = view.findViewById(R.id.login_to_register_tv)
        emailEt = view.findViewById(R.id.login_email_et)
        passwordEt = view.findViewById(R.id.login_password_et)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginViewModel.instance()
    }

    override fun onStart() {
        super.onStart()

        if (loginViewModel.instance() != null) {
            findNavController().navigate(R.id.action_loginFragment_to_calenderFragment2)
        }

        loginBtn.setOnClickListener {

            val email = emailEt.text.toString()
            val password = passwordEt.text.toString()

            when{
                email.isEmpty() -> showToast("Enter your email")
                password.isEmpty() -> showToast("Enter your password")
                else -> loginViewModel.login(email, password)
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