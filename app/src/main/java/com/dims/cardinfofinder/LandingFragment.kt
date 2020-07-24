package com.dims.cardinfofinder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.button.MaterialButton

class LandingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_landing, container, false)

        val inputButton = view.findViewById<MaterialButton>(R.id.inputButton)
        val scanButton = view.findViewById<MaterialButton>(R.id.scanButton)

        inputButton.setOnClickListener {
            val action = LandingFragmentDirections.actionLandingFragmentToInputFragment()
            NavHostFragment.findNavController(requireParentFragment()).navigate(action)
        }
        scanButton.setOnClickListener {
            val action = LandingFragmentDirections.actionLandingFragmentToScanFragment()
            NavHostFragment.findNavController(requireParentFragment()).navigate(action)
        }

        return view
    }
}