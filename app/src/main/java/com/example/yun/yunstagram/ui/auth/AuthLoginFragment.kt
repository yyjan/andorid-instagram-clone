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
import com.example.yun.yunstagram.databinding.FragmentAuthLoginBinding
import com.example.yun.yunstagram.ui.home.MainActivity
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_auth_login.*
import javax.inject.Inject

class AuthLoginFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var authViewModel: AuthViewModel

    companion object {
        fun newInstance() = AuthLoginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        authViewModel = ViewModelProviders.of(this, viewModelFactory).get(AuthViewModel::class.java)

        val binding = DataBindingUtil.inflate<FragmentAuthLoginBinding>(
            inflater, R.layout.fragment_auth_login, container, false
        ).apply {
            viewmodel = authViewModel
            lifecycleOwner = this@AuthLoginFragment
        }

        binding.viewmodel.let { loginViewModel ->
            authViewModel.loginResult.observe(this, Observer { state ->
                if (state.isSuccess) {
                    activity?.let { it ->
                        it.finish()
                        startActivity(Intent(activity, MainActivity::class.java))
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

        // TODO: remove test code
        et_email.setText("test@test.com")
        et_password.setText("test1234")

        btn_login.setOnClickListener {
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()
            authViewModel.login(email, password)
        }
    }

}
