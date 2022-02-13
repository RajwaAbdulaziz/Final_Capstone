

package com.tuwaiq.finalcapstone.presentation.registerFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.tuwaiq.finalcapstone.MyCallback
import com.tuwaiq.finalcapstone.R
import com.tuwaiq.finalcapstone.databinding.RegisterFragmentBinding
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "RegisterFragment"
@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: RegisterFragmentBinding
    private val registerViewModel by viewModels<RegisterViewModel>()

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
        //val view = inflater.inflate(R.layout.register_fragment, container, false)
        binding = RegisterFragmentBinding.inflate(inflater, container, false)
        //init(view)
        return binding.root
    }

    private fun init(view: View) {
        nameEt = view.findViewById(R.id.register_name_et)
        emailEt = view.findViewById(R.id.register_email_et)
        passwordEt = view.findViewById(R.id.register_password_et)
        confirmPasswordEt = view.findViewById(R.id.register_conf_pass_et)
        registerBtn = view.findViewById(R.id.register_btn)
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

        binding.registerBtn.setOnClickListener {
            val name = binding.registerNameEt.text.toString()
            val email = binding.registerEmailEt.text.toString()
            val pass = binding.registerPasswordEt.text.toString()
            val confPass = binding.registerConfPassEt.text.toString()

            when{
                name.isEmpty() -> showToast(resources.getString(R.string.plz_name))
                email.isEmpty() -> showToast(resources.getString(R.string.plz_email))
                pass.isEmpty() -> showToast(resources.getString(R.string.plz_pass))
                confPass != pass -> showToast(resources.getString(R.string.plz_match))
                else -> {
                    registerViewModel.register(name, email, pass, object : MyCallback{
                        override fun authResult(authResult: Task<AuthResult>) {
                            super.authResult(authResult)

                            if (authResult.isSuccessful) {
                                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                            } else {
                                Toast.makeText(context, resources.getString(R.string.oops), Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                }
            }
        }
    }



    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}