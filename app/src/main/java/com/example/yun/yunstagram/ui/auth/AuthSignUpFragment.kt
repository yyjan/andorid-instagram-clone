package com.example.yun.yunstagram.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.yun.yunstagram.R
import com.example.yun.yunstagram.databinding.FragmentAuthSignupBinding
import com.example.yun.yunstagram.ui.home.MainActivity
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_auth_signup.*
import javax.inject.Inject

class AuthSignUpFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var authViewModel: AuthViewModel

    companion object {
        fun newInstance() = AuthSignUpFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        authViewModel = ViewModelProviders.of(this, viewModelFactory).get(AuthViewModel::class.java)

        val binding = DataBindingUtil.inflate<FragmentAuthSignupBinding>(
            inflater, R.layout.fragment_auth_signup, container, false
        ).apply {
            viewmodel = authViewModel
            lifecycleOwner = this@AuthSignUpFragment
        }

        binding.viewmodel.let {
            authViewModel.loginResult.observe(this, Observer { state ->
                if (state.isSuccess) {
                    activity?.let { it ->
                        it.finish()
                        startActivity(Intent(it, MainActivity::class.java))
                    }
                } else {
                    Toast.makeText(activity, state.errorMessages, Toast.LENGTH_SHORT).show()
                }
            })
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_sign_up.setOnClickListener {
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()
            val userName = et_username.text.toString().trim()
            authViewModel.signup(email, password, userName)
        }
    }

}
