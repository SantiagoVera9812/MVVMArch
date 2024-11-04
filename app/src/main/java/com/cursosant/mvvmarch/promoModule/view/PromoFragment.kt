package com.cursosant.mvvmarch.promoModule.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cursosant.mvvmarch.BR
import com.cursosant.mvvmarch.common.local.FakeFirebaseAuth
import com.cursosant.mvvmarch.common.local.getAllPromos
import com.cursosant.mvvmarch.databinding.FragmentPromoBinding
import com.cursosant.mvvmarch.loginModule.model.LoginRepository
import com.cursosant.mvvmarch.loginModule.viewModel.LoginViewModel
import com.cursosant.mvvmarch.loginModule.viewModel.LoginViewModelfactory
import com.cursosant.mvvmarch.promoModule.model.Database
import com.cursosant.mvvmarch.promoModule.model.PromoRepository
import com.cursosant.mvvmarch.promoModule.viewModel.PromoViewModel
import com.cursosant.mvvmarch.promoModule.viewModel.PromoViewModelfactory
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
class PromoFragment : Fragment() {
    private var _binding: FragmentPromoBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PromoListAdapter
    private lateinit var vm: PromoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentPromoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupAdapter()
        setupRecyclerView()
        setupObservers()

    }


    private fun setupObservers() {

        binding.viewModel?.let { vm ->

            vm.snackbarMsg.observe(viewLifecycleOwner){
                    resMsg ->
                showMsg(resMsg)
            }

            vm.promos.observe(viewLifecycleOwner){
                promos ->
                adapter.submitList(promos)
            }



        }
    }

    private fun showMsg(resMsg: Int) {
        Snackbar.make(binding.root, resMsg, Snackbar.LENGTH_SHORT).show()
    }

    private fun setupViewModel() {
        vm = ViewModelProvider(this, PromoViewModelfactory(PromoRepository(Database())))[PromoViewModel::class.java]

        binding.lifecycleOwner = this
        binding.setVariable(BR.viewModel, vm)
    }

    private fun setupAdapter() {
        adapter = PromoListAdapter()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = this@PromoFragment.adapter
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}