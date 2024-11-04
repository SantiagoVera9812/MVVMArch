package com.cursosant.mvvmarch.loginModule.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cursosant.mvvmarch.BR
import com.cursosant.mvvmarch.mainActivity.view.MainActivity
import com.cursosant.mvvmarch.common.local.FakeFirebaseAuth
import com.cursosant.mvvmarch.databinding.FragmentLoginBinding
import com.cursosant.mvvmarch.loginModule.model.LoginRepository
import com.cursosant.mvvmarch.loginModule.viewModel.LoginViewModel
import com.cursosant.mvvmarch.loginModule.viewModel.LoginViewModelfactory
import com.google.android.material.snackbar.Snackbar

/****
 * Project: Wines
 * From: com.cursosant.wines
 * Created by Alain Nicolás Tello on 06/02/24 at 20:23
 * All rights reserved 2024.
 *
 * All my Udemy Courses:
 * https://www.udemy.com/user/alain-nicolas-tello/
 * And Frogames formación:
 * https://cursos.frogamesformacion.com/pages/instructor-alain-nicolas
 *
 * Coupons on my Website:
 * www.alainnicolastello.com
 ***/
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var vm: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupViewModel()
        setupButtons()
        setupObservers()
    }

    private fun setupViewModel() {
        vm = ViewModelProvider(this, LoginViewModelfactory(LoginRepository(FakeFirebaseAuth())))[LoginViewModel::class.java]

        binding.lifecycleOwner = this
        binding.setVariable(BR.viewModel, vm)
    }


    private fun setupObservers() {

        binding.viewModel?.let { vm ->

            vm.snackbarMsg.observe(viewLifecycleOwner){
                    resMsg ->
                showMsg(resMsg)
            }

            vm.isAuthValid.observe(viewLifecycleOwner){
                isValid ->
                Log.i("isValid", isValid.toString())
                if (isValid) closeLoginUI()
            }



        }
    }


    private fun setupButtons() {
        with(binding) {
         btnLogin.setOnClickListener {

                 vm.login(etUsername.text.toString(), etPin.text.toString())


//                lifecycleScope.launch {
//                    showProgress(true)
//                    showForm(false)
//                    val auth = FakeFirebaseAuth()
//                    if (auth.login(etUsername.text.toString(), etPin.text.toString()))
//                        closeLoginUI()
//                    else {
//                        showProgress(false)
//                        showMsg(R.string.login_login_fail)
//                        showForm(true)
//                    }
//                }
            }
        }
    }

    private fun showMsg(msgRes: Int) {
        Snackbar.make(binding.root, msgRes, Snackbar.LENGTH_SHORT).show()
    }


    private fun closeLoginUI() {
        (requireActivity() as MainActivity).setupNavView(true)
        requireActivity().supportFragmentManager.beginTransaction().apply {
            remove(this@LoginFragment)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}