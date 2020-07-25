package com.dims.cardinfofinder

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class InputFragment : Fragment() {
    private lateinit var textField: TextInputLayout
    private lateinit var viewModel: InputViewModel
    private lateinit var submitButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(InputViewModel::class.java)

        submitButton = view.findViewById(R.id.submitButton)
        textField = view.findViewById(R.id.cardNumber_textField)
        textField.editText?.doOnTextChanged { inputText, _, _, _ ->
            // Respond to input text change
            viewModel.textLiveData.postValue(
                (inputText.toString()).trim()
            )
        }

        viewModel.error.observe(viewLifecycleOwner, Observer {
            when(it){
                ErrorState.IDLE -> {
                    submitButton.isEnabled = false
                }
                ErrorState.VISIBLE -> {
                    textField.error = "8 digits expected"
                    submitButton.isEnabled = false
                }
                ErrorState.INVISIBLE -> {
                    textField.error = null
                    submitButton.isEnabled = true
                }
                else -> {
                    Log.e("InputFragment", "should not reach here")
                }
            }
        })

        submitButton.setOnClickListener {
            val action =
                InputFragmentDirections.actionInputFragmentToResultFragment(viewModel.value)
            NavHostFragment.findNavController(requireParentFragment()).navigate(action)
        }
    }

    override fun onStart() {
        super.onStart()
        with(viewModel.textLiveData.value){
            if (this?.isNotBlank() == true)
                textField.editText?.setText(this)
        }
    }
}