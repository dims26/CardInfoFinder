package com.dims.cardinfofinder.model

//brand or scheme
data class Card(val scheme: String, val type: String, val bank: Bank, val country: Country)

data class Bank(val name: String)

data class Country(val name: String)