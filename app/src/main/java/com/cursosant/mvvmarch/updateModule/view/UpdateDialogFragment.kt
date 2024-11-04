package com.cursosant.mvvmarch.updateModule.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.cursosant.mvvmarch.BR
import com.cursosant.mvvmarch.R
import com.cursosant.mvvmarch.WineApplication
import com.cursosant.mvvmarch.common.utils.Constants
import com.cursosant.mvvmarch.common.entities.Wine
import com.cursosant.mvvmarch.databinding.FragmentDialogUpdateBinding
import com.cursosant.mvvmarch.updateModule.model.RoomDatabase
import com.cursosant.mvvmarch.updateModule.model.UpdateRepository
import com.cursosant.mvvmarch.updateModule.viewModel.UpdateViewModel
import com.cursosant.mvvmarch.updateModule.viewModel.UpdateViewModelFactory


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
class UpdateDialogFragment : DialogFragment(), OnShowListener {

    private var _binding: FragmentDialogUpdateBinding? = null
    private val binding get() = _binding!!

    private var _wineId = -1.0
    private lateinit var wine: Wine
    private lateinit var vm: UpdateViewModel

    private var onUpdateListener: () -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val id = it.getDouble(Constants.ARG_ID, 0.0)
            _wineId = id
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentDialogUpdateBinding.inflate(layoutInflater)//LayoutInflater.from(requireContext()))

        val builder = AlertDialog.Builder(requireContext()).apply {
            setTitle(R.string.dialog_update_title)
            setPositiveButton(R.string.dialog_update_ok, null)
            setNegativeButton(R.string.dialog_update_cancel, null)
            setView(binding.root)
        }

        val dialog = builder.create()
        dialog.setOnShowListener(this)

        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?) = binding.root

    override fun onShow(dialogInterface: DialogInterface?) {
        val dialog = dialog as? AlertDialog
        dialog?.let {
            val positiveButton = it.getButton(DialogInterface.BUTTON_POSITIVE)
            val negativeButton = it.getButton(DialogInterface.BUTTON_NEGATIVE)
            positiveButton.setOnClickListener {

                vm.updateWine(binding.rating.rating.toString())

            }
            negativeButton.setOnClickListener { dismiss() }
        }

        setupViewModel()
        setupObservers()

        getWineById()
    }

    private fun setupViewModel() {
        vm = ViewModelProvider(this, UpdateViewModelFactory(UpdateRepository(RoomDatabase())))[UpdateViewModel::class.java]

        binding.lifecycleOwner = this
        binding.setVariable(BR.viewModel, vm)
    }

    private fun setupObservers() {

        binding.viewModel?.let { vm ->

            vm.snackbarMsg.observe(viewLifecycleOwner){
                    resMsg ->
                showMsg(resMsg)
                if (resMsg == R.string.room_save_success) dismiss()
            }

        }
    }

    private fun showMsg(msgRes: Int) {
        Toast.makeText(requireContext(), msgRes, Toast.LENGTH_SHORT).show()
    }


    private fun getWineById() {

        if(_wineId != -1.0) vm.requestWine(_wineId)

    }

    fun setOnUpdateListener(block: () -> Unit) {
        onUpdateListener = block
    }

    override fun onDismiss(dialog: DialogInterface) {
        onUpdateListener()
        super.onDismiss(dialog)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}