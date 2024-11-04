package com.cursosant.mvvmarch.favouriteModule.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cursosant.mvvmarch.BR
import com.cursosant.mvvmarch.updateModule.view.UpdateDialogFragment
import com.cursosant.mvvmarch.common.utils.Constants
import com.cursosant.mvvmarch.common.utils.OnClickListener
import com.cursosant.mvvmarch.common.entities.Wine
import com.cursosant.mvvmarch.common.view.WineBaseFragment
import com.cursosant.mvvmarch.favouriteModule.model.FavouriteRepository
import com.cursosant.mvvmarch.favouriteModule.model.RoomDatabase
import com.cursosant.mvvmarch.favouriteModule.viewModel.FavouriteViewModel
import com.cursosant.mvvmarch.favouriteModule.viewModel.FavouriteViewModelFactory
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
class FavouriteFragment : WineBaseFragment(), OnClickListener {

    private lateinit var adapter: WineFavListAdapter
    private lateinit var vm: FavouriteViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupAdapter()
        setupRecyclerView()
        //setupSwipeRefresh()
        setupObservers()
    }

    private fun setupViewModel() {
        vm = ViewModelProvider(this,
            FavouriteViewModelFactory(FavouriteRepository(RoomDatabase())))[FavouriteViewModel::class.java]

        binding.lifecycleOwner = this
        binding.setVariable(BR.viewModel, vm)
    }

    private fun setupAdapter() {
        adapter = WineFavListAdapter()
        adapter.setOnClickListener(this)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = this@FavouriteFragment.adapter
        }
    }

    private fun setupObservers() {

        binding.viewModel?.let { vm ->

            vm.snackbarMsg.observe(viewLifecycleOwner){
                    resMsg ->
                showMsg(resMsg)
            }

            vm.wines.observe(viewLifecycleOwner){ wines ->
                adapter.submitList(wines)
            }



        }
    }

//    private fun setupSwipeRefresh() {
//        binding.srlResults.setOnRefreshListener {
//            getWines()
//        }
//    }

//    private fun getWines() {
//        lifecycleScope.launch(Dispatchers.IO) {
//            try {
//                val wines = WineApplication.database.wineDao().getAllWines()
//                withContext(Dispatchers.Main){
//                    /*adapter.submitList(null)//temporal solution, fix it with architectures
//                    adapter.submitList(wines)*/
//                    if (wines.isNotEmpty()) {
//                        showNoDataView(false)
//                        showRecyclerView(true)
//                        adapter.submitList(wines)
//                    } else {
//                        showRecyclerView(false)
//                        showNoDataView(true)
//                    }
//                }
//            } catch (e: Exception) {
//                showMsg(R.string.room_request_fail)
//            } finally {
//                showProgress(false)
//            }
//        }
//    }

    private fun showMsg(msgRes: Int) {
        Snackbar.make(binding.root, msgRes, Snackbar.LENGTH_SHORT).show()
    }
//
//    private fun showProgress(isVisible: Boolean) {
//        binding.srlResults.isRefreshing = isVisible
//    }
//
//    private fun showNoDataView(isVisible: Boolean) {
//        binding.tvNoData.visibility = if (isVisible) View.VISIBLE else View.GONE
//    }
//
//    private fun showRecyclerView(isVisible: Boolean) {
//        binding.recyclerView.visibility = if (isVisible) View.VISIBLE else View.GONE
//    }

//    override fun onResume() {
//        super.onResume()
//        showProgress(true)
//        getWines()
//    }

    /*
    * OnClickListener
    * */
    override fun onFavorite(wine: Wine) {
        wine.isFavorite = !wine.isFavorite

        if (wine.isFavorite) vm.addWine(wine) else vm.deleteWine(wine)
//        lifecycleScope.launch(Dispatchers.IO) {
//            if (wine.isFavorite){
//                val result = WineApplication.database.wineDao().addWine(wine)
//                if (result == -1L) {
//                    Snackbar.make(binding.root, R.string.room_save_fail, Snackbar.LENGTH_SHORT).show()
//                } else {
//                    Snackbar.make(binding.root, R.string.room_save_success, Snackbar.LENGTH_SHORT).show()
//                }
//            } else {
//                val result = WineApplication.database.wineDao().deleteWine(wine)
//                if (result == 0) {
//                    Snackbar.make(binding.root, R.string.room_save_fail, Snackbar.LENGTH_SHORT).show()
//                } else {
//                    Snackbar.make(binding.root, R.string.room_save_success, Snackbar.LENGTH_SHORT).show()
//                }
//            }
//        }
    }

    override fun onLongClick(wine: Wine) {
        val fragmentManager = childFragmentManager//requireActivity().supportFragmentManager
        val fragment = UpdateDialogFragment()
        val args = Bundle()
        args.putDouble(Constants.ARG_ID, wine.id)
        fragment.arguments = args
        fragment.show(fragmentManager, UpdateDialogFragment::class.java.simpleName)
        fragment.setOnUpdateListener {
            binding.srlResults.isRefreshing = true
            //getWines()
            binding.viewModel?.getAllWines()
        }
    }
}