package com.xeniac.dsfut.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.xeniac.dsfut.R
import com.xeniac.dsfut.databinding.FragmentPickUpBinding
import com.xeniac.dsfut.ui.viewmodels.MainViewModel

class PickUpFragment : Fragment(R.layout.fragment_pick_up) {

    private var _binding: FragmentPickUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPickUpBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }
}