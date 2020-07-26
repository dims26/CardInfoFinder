package com.dims.cardinfofinder.screens.input

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.dims.cardinfofinder.R
import com.dims.cardinfofinder.helpers.ErrorState
import com.dims.cardinfofinder.helpers.ViewModelFactory
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

        val factory = ViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(InputViewModel::class.java)

        submitButton = view.findViewById(R.id.submitButton)
        textField = view.findViewById(R.id.cardNumber_textField)
        showSoftKeyboard(textField.editText)
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
                InputFragmentDirections.actionInputFragmentToResultFragment(
                    viewModel.value
                )
            hideSoftKeyboard(textField.editText)
            NavHostFragment.findNavController(this).navigate(action)
        }
    }

    private fun showSoftKeyboard(textField: EditText?) {
        if (textField?.requestFocus() == true) {
            val imm = textField.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.showSoftInput(textField, InputMethodManager.SHOW_FORCED)
        }
    }
    private fun hideSoftKeyboard(textField: EditText?) {
        if (textField?.hasFocus() == true) {
            val imm = textField.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(textField.windowToken, 0)
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