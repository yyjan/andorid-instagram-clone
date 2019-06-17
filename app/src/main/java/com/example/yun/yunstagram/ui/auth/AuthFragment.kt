package com.example.yun.yunstagram.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.yun.yunstagram.R
import com.example.yun.yunstagram.databinding.FragmentAuthBinding
import com.example.yun.yunstagram.ui.home.MainActivity
import com.example.yun.yunstagram.utilities.addFragment
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_auth.*
import javax.inject.Inject


class AuthFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var authViewModel: AuthViewModel

    companion object {
        fun newInstance() = AuthFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        authViewModel = ViewModelProviders.of(this, viewModelFactory).get(AuthViewModel::class.java)

        val binding = DataBindingUtil.inflate<FragmentAuthBinding>(
            inflater, R.layout.fragment_auth, container, false
        ).apply {
            viewmodel = authViewModel
            lifecycleOwner = this@AuthFragment
        }

        authViewModel.autoLoginState.observe(this, Observer { isAutoLogin ->
            if (isAutoLogin) {
                activity?.finish()
                startActivity(Intent(activity, MainActivity::class.java))
            }
        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        authViewModel.checkAutoLogin()

        btn_create_account.setOnClickListener {
            addFragment(activity!!, AuthSignUpFragment(), R.id.contentFrame, "AuthSignUpFragment")
        }

        btn_login.setOnClickListener {
            addFragment(activity!!, AuthLoginFragment(), R.id.contentFrame, "AuthLoginFragment")
        }

    }

}
