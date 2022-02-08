package com.tuwaiq.finalcapstone.presentation.loginFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.tuwaiq.finalcapstone.MyCallback
import com.tuwaiq.finalcapstone.R
import com.tuwaiq.finalcapstone.databinding.LoginFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: LoginFragmentBinding
    private lateinit var loginBtn: Button
    private lateinit var registerTv: TextView
    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val view = inflater.inflate(R.layout.login_fragment, container, false)
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        //init(view)
        return binding.root
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
            findNavController().navigate(R.id.action_loginFragment_to_listFragment2)
        }

        binding.loginBtn.setOnClickListener {

            val email = binding.loginEmailEt.text.toString()
            val password = binding.loginPasswordEt.text.toString()

            when{
                email.isEmpty() -> showToast("Enter your email")
                password.isEmpty() -> showToast("Enter your password")
                else -> {
                    loginViewModel.login(email, password, object : MyCallback{
                        override fun authResult(authResult: Task<AuthResult>) {
                            super.authResult(authResult)

                            if (authResult.isSuccessful) {
                                findNavController().navigate(R.id.action_loginFragment_to_listFragment2)
                            } else {
                                Toast.makeText(context, "Wrong info", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                }
            }
        }

        binding.loginToRegisterTv.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}