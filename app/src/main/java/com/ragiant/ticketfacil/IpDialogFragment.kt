package com.ragiant.ticketfacil

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.ragiant.ticketfacil.databinding.FragmentIpDialogBinding

class IpDialogFragment constructor(
    private val ipOld:String,
    private val onClick:(ip:String)->Unit,
) : DialogFragment() {
    private lateinit var binding:FragmentIpDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog{
        val builder = AlertDialog.Builder(
            requireContext()
        )
        binding = FragmentIpDialogBinding.inflate(requireActivity().layoutInflater)
        binding.tvIp.setText(ipOld)
        isCancelable = false

        builder.setView(binding.root).setTitle(requireContext().getString(R.string.dialog_puntosh_title))
        builder.setPositiveButton(
            requireContext().getString(R.string.guardar)
        ) { dialog, id ->
            onClick(binding.tvIp.text.toString())
            dismiss()
        }
        return builder.create()
    }
}