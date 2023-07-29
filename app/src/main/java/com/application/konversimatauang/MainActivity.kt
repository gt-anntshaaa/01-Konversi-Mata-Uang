package com.application.konversimatauang

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.application.konversimatauang.databinding.ActivityMainBinding
import com.application.konversimatauang.helper.EndPoint
import com.application.konversimatauang.helper.Resource
import com.application.konversimatauang.helper.Utility
import com.application.konversimatauang.model.Rates
import com.application.konversimatauang.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Currency
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    private var selectedItem1: String? = "AFN"
    private var selectedItem2: String? = "AFN"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        //setTheme(R.style.AppTheme_NoActionBar);

        super.onCreate(savedInstanceState)
        //Make status bar transparent
        Utility.makeStatusBarTransparan(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSpinner()

        //Listen to click events
        setUpClickListener()


    }

    private fun setUpClickListener() {
        binding.btnConvert.setOnClickListener {
            //check if the input is empty
            val numberToConvert = binding.etFirstCurrency.text.toString()

            if(numberToConvert.isEmpty() || numberToConvert == "0"){
                Snackbar.make(binding.mainLayout,"Input a value in the first text field, result will be shown in the second text field", Snackbar.LENGTH_LONG)
                    .withColor(ContextCompat.getColor(this, R.color.dark_red))
                    .setTextColor(ContextCompat.getColor(this, R.color.white))
                    .show()
            }//check if internet is available
            else if (!Utility.isNetworkAvailable(this)){
                Snackbar.make(binding.mainLayout,"You are not connected to the internet", Snackbar.LENGTH_LONG)
                    .withColor(ContextCompat.getColor(this, R.color.dark_red))
                    .setTextColor(ContextCompat.getColor(this, R.color.white))
                    .show()
            }else{
                doConversion()
            }
        }
    }

    private fun doConversion() {
        Utility.hideKeyboard(this)
        //make progress bar visible
        binding.prgLoading.visibility = View.VISIBLE

        //make button invisible
        binding.btnConvert.visibility = View.GONE

        //Get the data inputed
        val apiKey = EndPoint.API_KEY
        val from = selectedItem1.toString()
        val to = selectedItem2.toString()
        val amount = binding.etFirstCurrency.text.toString().toDouble()

        // do the conversion
        viewModel.getConvertedData(apiKey, from, to, amount)

        observeUI()
    }

    private fun observeUI() {
        viewModel.data.observe(this, Observer {result ->
            when(result){
                is Resource.Success -> {
                    val map: Map<String, Rates>

                    map = result.data.rates

                    map.keys.forEach {

                        val rateForAmount = map[it]?.rate_for_amount

                        viewModel.convertedRate.value = rateForAmount

                        //format the result obtained e.g 1000 = 1,000
                        val formattedString = String.format("%,.2f", viewModel.convertedRate.value)

                        //set the value in the second edit text field
                        binding.etSecondCurrency.setText(formattedString)

                    }

                    //stop progress bar
                    binding.prgLoading.visibility = View.GONE
                    //show button
                    binding.btnConvert.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    val layout = binding.mainLayout
                    Snackbar.make(layout,  "Oopps! Something went wrong, Try again", Snackbar.LENGTH_LONG)
                        .withColor(ContextCompat.getColor(this, R.color.dark_red))
                        .setTextColor(ContextCompat.getColor(this, R.color.white))
                        .show()
                    //stop progress bar
                    binding.prgLoading.visibility = View.GONE
                    //show button
                    binding.btnConvert.visibility = View.VISIBLE
                }

                is Resource.Loading -> {
                    //stop progress bar
                    binding.prgLoading.visibility = View.VISIBLE
                    //show button
                    binding.btnConvert.visibility = View.GONE
                }
                else -> {}
            }
        })
    }

    private fun Snackbar.withColor(@ColorInt colorInt: Int): Snackbar {
        this.view.setBackgroundColor(colorInt)
        return this
    }


    private fun initSpinner(){
        val spinner1 = binding.spnFirstCountry

        spinner1.setItems(getAllCountries())

        spinner1.setOnClickListener {
            Utility.hideKeyboard(this)
        }

        spinner1.setOnItemSelectedListener { view, position, id, item ->
            val countryCode = getCountryCode(item.toString())
            val currencySymbol = getSymbol(countryCode)
            selectedItem1 = currencySymbol
            binding.txtFirstCurrencyName.text = selectedItem1
        }


        val spinner2 = binding.spnSecondCountry

        //hide key board when spinner shows
        spinner1.setOnClickListener {
            Utility.hideKeyboard(this)
        }

        //set items on second spinner i.e - a list of all countries
        spinner2.setItems( getAllCountries() )

        //Handle selected item, by getting the item and storing the value in a  variable - selectedItem2,
        spinner2.setOnItemSelectedListener { view, position, id, item ->
            //Set the currency code for each country as hint
            val countryCode = getCountryCode(item.toString())
            val currencySymbol = getSymbol(countryCode)
            selectedItem2 = currencySymbol
            binding.txtSecondCurrencyName.text = selectedItem2
        }
    }

    private fun getSymbol(countryCode: String?): String?{
        val availableLocales = Locale.getAvailableLocales()
        for (i in availableLocales.indices){
            if (availableLocales[i].country == countryCode)
                return Currency.getInstance(availableLocales[i]).currencyCode
        }
        return ""
    }

    private fun getCountryCode(countryName: String) = Locale.getISOCountries().find {
        Locale("",it).displayCountry == countryName
    }

    private fun getAllCountries(): ArrayList<String>{
        val locales = Locale.getAvailableLocales()
        val countries = ArrayList<String>()

        for (locale in locales){
            val country = locale.displayCountry
            if (country.trim { it <= ' ' }.isNotEmpty() && !countries.contains(country)){
                countries.add(country)
            }
        }
        countries.sort()

        return countries
    }
}