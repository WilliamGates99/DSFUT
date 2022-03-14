package com.xeniac.dsfut.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.xeniac.dsfut.R
import com.xeniac.dsfut.databinding.FragmentPickUpBinding

class PickUpFragment : Fragment(R.layout.fragment_pick_up) {

    private var _binding: FragmentPickUpBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPickUpBinding.bind(view)
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