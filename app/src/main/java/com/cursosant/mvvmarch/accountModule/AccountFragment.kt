package com.cursosant.mvvmarch.accountModule

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.cursosant.mvvmarch.BR
import com.cursosant.mvvmarch.common.utils.Constants
import com.cursosant.mvvmarch.common.local.FakeFirebaseAuth
import com.cursosant.mvvmarch.mainActivity.view.MainActivity
import com.cursosant.mvvmarch.R
import com.cursosant.mvvmarch.accountModule.model.AccountRepository
import com.cursosant.mvvmarch.accountModule.viewModel.AccountViewModel
import com.cursosant.mvvmarch.accountModule.viewModel.AccountViewModelFactory
import com.cursosant.mvvmarch.databinding.FragmentAccountBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

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
class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var vm: AccountViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupIntents()
        setupObservers()
//        setupUserUI()
//        setupButtons()

    }

    private fun setupObservers() {

        binding.viewModel?.let { vm ->

            vm.snackbarmsg.observe(viewLifecycleOwner){
                resMsg ->
                showMsg(resMsg)
            }

            vm.isSignOut.observe(viewLifecycleOwner){isSignout ->

                if (isSignout){

                    (requireActivity() as MainActivity).apply {
                        setupNavView(false)
                        launchLoginUI()
                    }
                }

            }

        }
    }

    private fun setupViewModel(){
        vm = ViewModelProvider(this, AccountViewModelFactory(AccountRepository(FakeFirebaseAuth())))[AccountViewModel::class.java]

        binding.lifecycleOwner = this
        binding.setVariable(BR.viewModel, vm)
    }

    private fun setupUserUI() {
        val auth = FakeFirebaseAuth()
        lifecycleScope.launch {
            showProgress(true)
            auth.getCurrentUser()?.let { user ->
                with(binding) {
                    tvName.text = user.displayName
                    tvEmail.text = user.email
                    tvPhone.text = user.phone

                    Glide.with(requireContext())
                        .load(user.photoUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(imgProfile)
                }
                //setupIntents()
            }
            showProgress(false)
        }
    }

    private fun setupIntents() {
        binding.tvEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse(Constants.DATA_MAIL_TO)
                putExtra(Intent.EXTRA_EMAIL, arrayOf(binding.tvEmail.text.toString()))
                putExtra(Intent.EXTRA_SUBJECT, "From kotlin architectures course")
            }
            launchIntent(intent)
        }

        binding.tvPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                val phone = (it as TextView).text
                data = Uri.parse("${Constants.DATA_TEL}$phone")
            }
            launchIntent(intent)
        }
    }

    private fun launchIntent(intent: Intent){
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(requireActivity(), getString(R.string.account_error_no_resolve), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupButtons() {
        binding.btnSignOut.setOnClickListener {
            lifecycleScope.launch {
                showProgress(true)
                val auth = FakeFirebaseAuth()
                if (auth.signOut()){
                    (requireActivity() as MainActivity).apply {
                        setupNavView(false)
                        launchLoginUI()
                    }
                    showProgress(false)
                } else {
                    showProgress(false)
                    showMsg(R.string.account_sign_out_fail)
                }
            }
        }
    }

    private fun showMsg(msgRes: Int) {
        Snackbar.make(binding.root, msgRes, Snackbar.LENGTH_SHORT).show()
    }

    private fun showProgress(isVisible: Boolean) {
        binding.contentProgress.root.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}