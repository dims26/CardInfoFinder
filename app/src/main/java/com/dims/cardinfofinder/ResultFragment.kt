package com.dims.cardinfofinder

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import java.util.*

class ResultFragment : Fragment() {
    private lateinit var cardNumberTextView: TextView
    private lateinit var cardBrandTextView: TextView
    private lateinit var cardTypeTextView: TextView
    private lateinit var bankNameTextView: TextView
    private lateinit var countryTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var detailLayout: LinearLayout
    private lateinit var viewModel: ResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view){
            cardNumberTextView = findViewById(R.id.card_number)
            cardBrandTextView = findViewById(R.id.card_brand)
            cardTypeTextView = findViewById(R.id.card_type)
            bankNameTextView = findViewById(R.id.bank_name)
            countryTextView = findViewById(R.id.country)
            detailLayout = findViewById(R.id.detail_block)
            progressBar = findViewById(R.id.detail_progressBar)
        }

        val cardNumber = arguments?.getInt("cardNumber")!!

        val factory = ViewModelFactory(requireActivity().application, cardNumber)
        viewModel = ViewModelProvider(this, factory).get(ResultViewModel::class.java)

        val cardService = ServiceBuilder.buildService(CardService::class.java)
        viewModel.loadCardDetails(cardService)

        viewModel.isVisible.observe(viewLifecycleOwner, Observer {state ->
            when(state){
                NetworkState.ERROR -> {
                    detailLayout.visibility = View.INVISIBLE
                    progressBar.visibility = View.GONE
                    Snackbar.make(requireView(),
                        "Loading failed, check Internet access and Card number.",
                        Snackbar.LENGTH_INDEFINITE
                    ).show()
                }
                NetworkState.LOADED -> {
                    progressBar.visibility = View.GONE
                    detailLayout.visibility = View.VISIBLE
                }
                NetworkState.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    detailLayout.visibility = View.INVISIBLE
                }
                else -> {
                    Log.e("ResultFragment", "should not reach here")
                }
            }
        })

        viewModel.cardLiveData.observe(viewLifecycleOwner, Observer { card ->
            with(card){
                cardBrandTextView.text = scheme.toUpperCase(Locale.ROOT)
                cardTypeTextView.text = type.toUpperCase(Locale.ROOT)
                bankNameTextView.text = bank.name.toUpperCase(Locale.ROOT)
                countryTextView.text = country.name.toUpperCase(Locale.ROOT)

                cardNumberTextView.text = viewModel.cardNumber.toString()
            }
        })
    }
}