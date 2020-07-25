package com.dims.cardinfofinder

//brand or scheme
data class Card(val scheme: String, val type: String, val bank: Bank, val country: Country)

data class Bank(val name: String)

data class Country(val name: String)