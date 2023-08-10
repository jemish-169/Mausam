package com.client.mausam

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.client.mausam.API.RetrofitHelper
import com.client.mausam.Adapter.SearchCityAdapter
import com.client.mausam.Models.SearchListItem
import com.google.android.material.appbar.CollapsingToolbarLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


const val EXTRA_CITY_NAME = "EXTRA_CITY_NAME"

class MainActivity : AppCompatActivity() {

    // declared Api Key and LayoutManager
    val apiKey = BuildConfig.API_KEY //apiKey is hidden for github
    lateinit var linearLayoutManager: LinearLayoutManager
    val customCityList = mutableListOf(
        SearchListItem(
            id = "1105544",
            name = "Amreli",
            region = "Gujarat",
            country = "India",
            lat = 21.62,
            lon = 71.23,
            url = "amreli-gujarat-india"
        ), SearchListItem(
            id = "1129766",
            name = "Rajkot",
            region = "Gujarat",
            country = "India",
            lat = 22.3,
            lon = 70.78,
            url = "rajkot-gujarat-india"
        ),
        SearchListItem(
            id = "1125257",
            name = "Mumbai",
            region = "Maharashtra",
            country = "India",
            lat = 18.98,
            lon = 72.83,
            url = "mumbai-maharashtra-india"
        ),
        SearchListItem(
            id = "1129302",
            name = "Pune",
            region = "Maharashtra",
            country = "India",
            lat = 18.53,
            lon = 73.87,
            url = "pune-maharashtra-india"
        ), SearchListItem(
            id = "1126581",
            name = "New Delhi",
            region = "Delhi",
            country = "India",
            lat = 28.6,
            lon = 77.2,
            url = "new-delhi-delhi-india"
        ), SearchListItem(
            id = "1120848",
            name = "Kolkata",
            region = "West Bengal",
            country = "India",
            lat = 22.57,
            lon = 88.37,
            url = "kolkata-west-bengal-india"
        ), SearchListItem(
            id = "1116530",
            name = "Hyderabad",
            region = "Andhra Pradesh",
            country = "India",
            lat = 17.38,
            lon = 78.47,
            url = "hyderabad-andhra-pradesh-india"
        ), SearchListItem(
            "3333137",
            "Noida",
            "Uttar Pradesh",
            "India",
            28.57,
            77.32,
            "noida-uttar-pradesh-india"
        ),
        SearchListItem(
            "1107187",
            "Bangalore",
            "Karnataka",
            "India",
            12.98,
            77.58,
            "bangalore-karnataka-india"
        ),
        SearchListItem(
            "1129586",
            "Raichur",
            "Karnataka",
            "India",
            16.2,
            77.37,
            "raichur-karnataka-india"
        )
    )

    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Mausam)
        setContentView(R.layout.activity_main)

        //adding typeface for giving font-family to collapsible toolbar
        val typeface = ResourcesCompat.getFont(applicationContext, R.font.nunitosans_bold)
        val typeface_2 =
            ResourcesCompat.getFont(applicationContext, R.font.nunitosans_extrabold)
        val mainCollapsing = findViewById<CollapsingToolbarLayout>(R.id.main_collapsing)
        mainCollapsing.setCollapsedTitleTypeface(typeface_2)
        mainCollapsing.setExpandedTitleTypeface(typeface)

        var recyclerView: RecyclerView
        recyclerView = findViewById(R.id.city_rview)

        recyclerView.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        //Manage Search Box, after 3rd Alphabet it will start searching
        var searchText: AppCompatEditText? = null
        searchText = findViewById(R.id.edtSearch)

        //to show custom list when search box is empty
        val searchCitiesAdapter = SearchCityAdapter(
            customCityList,
            object : SearchCityAdapter.ItemClickListener {
                override fun onItemClick(city: SearchListItem) {
                    val intent =
                        Intent(this@MainActivity, DetailActivity::class.java)
                    intent.putExtra(EXTRA_CITY_NAME, city.name)
                    startActivity(intent)
                }
            })

        var recyclerView_2: RecyclerView
        recyclerView_2 = findViewById(R.id.city_rview)
        recyclerView_2.adapter = searchCitiesAdapter

        searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
//                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 2) {
                    AlldataSearch(s)
                } else {
                    //to show custom list when search box is empty
                    val searchCitiesAdapter = SearchCityAdapter(
                        customCityList,
                        object : SearchCityAdapter.ItemClickListener {
                            override fun onItemClick(city: SearchListItem) {
                                val intent =
                                    Intent(this@MainActivity, DetailActivity::class.java)
                                intent.putExtra(EXTRA_CITY_NAME, city.name)
                                startActivity(intent)
                            }
                        })

                    var recyclerView: RecyclerView
                    recyclerView = findViewById(R.id.city_rview)
                    recyclerView.adapter = searchCitiesAdapter
                }

            }

            override fun afterTextChanged(s: Editable?) {
//                TODO("Not yet implemented")
            }
        })
    }

    private fun AlldataSearch(s: CharSequence?) {

        //get data from search api
        val Api_interface = RetrofitHelper.create()

        Api_interface.searchLocation(apiKey, s.toString())
            .enqueue(object : Callback<List<SearchListItem>> {
                override fun onResponse(
                    call: Call<List<SearchListItem>>, response: Response<List<SearchListItem>>
                ) {
                    if (response.isSuccessful) {
                        var cityList = response.body()
                        if (cityList.isNullOrEmpty()) {
                            //empty body of if
                        } else {
                            val searchCitiesAdapter = SearchCityAdapter(
                                cityList,
                                object : SearchCityAdapter.ItemClickListener {
                                    override fun onItemClick(city: SearchListItem) {
                                        val intent =
                                            Intent(
                                                this@MainActivity,
                                                DetailActivity::class.java
                                            )
                                        intent.putExtra(EXTRA_CITY_NAME, city.name)
                                        startActivity(intent)
                                    }
                                })

                            var recyclerView: RecyclerView
                            recyclerView = findViewById(R.id.city_rview)
                            recyclerView.adapter = searchCitiesAdapter
                        }
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Response Failure",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                override fun onFailure(call: Call<List<SearchListItem>>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Network Failure", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

}