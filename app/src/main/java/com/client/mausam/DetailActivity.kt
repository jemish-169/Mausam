package com.client.mausam

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.client.mausam.API.RetrofitHelper
import com.client.mausam.Adapter.DailyForecastAdapter
import com.client.mausam.Models.DetailList_city
import com.client.mausam.Models.ForecastDetails
import com.client.mausam.Models.Hour
import com.google.android.material.appbar.CollapsingToolbarLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        val apiKey = BuildConfig.API_KEY //apiKey is hidden for github
        val aqi = "yes"
        val days = 10
        val alerts = "no"

        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Mausam)
        setContentView(R.layout.activity_detail)

        //get name from intent
        val cityName = intent.getStringExtra(EXTRA_CITY_NAME)

        val Api_interface = RetrofitHelper.create()

        //give typeface to collapsible layout
        val detail_typeface =
            ResourcesCompat.getFont(applicationContext, R.font.nunitosans_semibold)
        val detail_typeface_2 =
            ResourcesCompat.getFont(applicationContext, R.font.nunitosans_extrabold)
        val detail_Collapsing = findViewById<CollapsingToolbarLayout>(R.id.detailAc_collapsing)
        detail_Collapsing.setCollapsedTitleTypeface(detail_typeface_2)
        detail_Collapsing.setExpandedTitleTypeface(detail_typeface)

        //initialize variables for views
        var city_temp = findViewById<TextView>(R.id.city_Temp)
        var city_condition = findViewById<TextView>(R.id.city_condition)

        var detail_city_last_updated = findViewById<TextView>(R.id.detail_city_last_updated)
        var detail_city_country = findViewById<TextView>(R.id.detail_city_country)
        var detail_city_curr_img = findViewById<ImageView>(R.id.detail_city_curr_img)

        var detail_city_wind_speed = findViewById<TextView>(R.id.detail_city_wind_speed)
        var detail_city_wind_degree = findViewById<TextView>(R.id.detail_city_wind_degree)
        var detail_city_wind_direction = findViewById<TextView>(R.id.detail_city_wind_direction)
        var detail_city_air_preassure = findViewById<TextView>(R.id.detail_city_air_preassure)

        var detail_city_humidity = findViewById<TextView>(R.id.detail_city_humidity)
        var detail_city_cloud = findViewById<TextView>(R.id.detail_city_cloud)
        var detail_city_uv_index = findViewById<TextView>(R.id.detail_city_uv_index)
        var detail_city_precip = findViewById<TextView>(R.id.detail_city_precip)
        var detail_city_visibility = findViewById<TextView>(R.id.detail_city_visibility)

        var detail_city_sunrise = findViewById<TextView>(R.id.detail_city_sunrise)
        var detail_city_sunset = findViewById<TextView>(R.id.detail_city_sunset)
        var detail_city_moonrise = findViewById<TextView>(R.id.detail_city_moonrise)
        var detail_city_moonset = findViewById<TextView>(R.id.detail_city_moonset)
        var detail_city_moon_phase = findViewById<TextView>(R.id.detail_city_moon_phase)
        var detail_city_moon_illumination =
            findViewById<TextView>(R.id.detail_city_moon_illumination)


        //Make an API call for current weather
        Api_interface.getDetailedWeather(apiKey, cityName.toString(), aqi)
            .enqueue(object : Callback<DetailList_city> {
                //Handle Response
                override fun onResponse(
                    call: Call<DetailList_city>, response: Response<DetailList_city>
                ) {
                    var res = response.body()
                    if (res != null) {

                        detail_Collapsing.title = cityName
                        city_temp.text = "${res.current.temp_c}°C"
                        city_condition.text = res.current.condition.text

                        detail_city_country.text = res.location.country
                        detail_city_last_updated.text = res.current.last_updated
                        var code = res.current.condition.icon.subSequence(41, 44).toString()
                        var icon = getIcon(code)
                        detail_city_curr_img.setImageResource(icon)


                        detail_city_wind_speed.text = res.current.wind_kph.toString() + " km/h"
                        detail_city_wind_degree.text = res.current.wind_degree.toString()
                        detail_city_wind_direction.text = res.current.wind_dir
                        detail_city_air_preassure.text = res.current.pressure_mb.toString() + " mb"

                        detail_city_humidity.text = res.current.humidity.toString() + " g.m-3"
                        detail_city_cloud.text = res.current.cloud.toString() + " oktas"
                        detail_city_uv_index.text = res.current.uv.toString()
                        detail_city_precip.text = res.current.precip_mm.toString() + "mm"
                        detail_city_visibility.text = res.current.vis_km.toString() + " km"

                    } else {
                        Toast.makeText(this@DetailActivity, "Response Failure", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                //Handle Failure
                override fun onFailure(call: Call<DetailList_city>, t: Throwable) {
                    Toast.makeText(this@DetailActivity, "Network Failure", Toast.LENGTH_SHORT)
                        .show()
                }

            })


        //Make API call for Future forecast
        Api_interface.getDetailedForecast(apiKey, cityName.toString(), days, aqi, alerts)
            .enqueue(object : Callback<ForecastDetails> {
                override fun onResponse(
                    call: Call<ForecastDetails>, response: Response<ForecastDetails>
                ) {
                    var res = response.body()
                    if (res != null) {
                        detail_city_sunrise.text = res.forecast.forecastday[0].astro.sunrise
                        detail_city_sunset.text = res.forecast.forecastday[0].astro.sunset
                        detail_city_moonrise.text = res.forecast.forecastday[0].astro.moonrise
                        detail_city_moonset.text = res.forecast.forecastday[0].astro.moonset
                        detail_city_moon_phase.text = res.forecast.forecastday[0].astro.moon_phase
                        detail_city_moon_illumination.text =
                            res.forecast.forecastday[0].astro.moon_illumination

                        //define Mainlist as arraylist for getting store data of hourly forecast
                        var mainList: List<Hour> = res.forecast.forecastday[0].hour
                        var tom_mainList: List<Hour> = res.forecast.forecastday[1].hour
                        var ovtom_mainList: List<Hour> = res.forecast.forecastday[2].hour

                        //add today's forecast
                        if (!(mainList.isEmpty())) {
                            val dailyForecastAdapter = DailyForecastAdapter(mainList,
                                object : DailyForecastAdapter.ItemClickListener {
                                    @SuppressLint("SetTextI18n")
                                    override fun onItemClick(hour: Hour) {

                                        val builder =
                                            androidx.appcompat.app.AlertDialog.Builder(this@DetailActivity)
                                        val popview: View =
                                            layoutInflater.inflate(R.layout.dialouge_box, null)
                                        builder.setView(popview)

                                        popview.findViewById<TextView>(R.id.dlg_city_Temp).text =
                                            hour.temp_c.toString() + "°C"

                                        var code = hour.condition.icon.subSequence(41, 44)
                                            .toString()
                                        var icon = getIcon(code)
                                        popview.findViewById<ImageView>(R.id.dlg_city_curr_img)
                                            .setImageResource(icon)

                                        popview.findViewById<TextView>(R.id.dlg_city_condition).text =
                                            hour.condition.text.toString()

                                        popview.findViewById<TextView>(R.id.dlg_city_wind_speed).text =
                                            hour.wind_kph.toString() + " km/h"

                                        popview.findViewById<TextView>(R.id.dlg_city_air_preassure).text =
                                            hour.pressure_mb.toString() + " mb"

                                        popview.findViewById<TextView>(R.id.dlg_city_humidity).text =
                                            hour.humidity.toString() + " g.m-3"

                                        popview.findViewById<TextView>(R.id.dlg_city_wind_direction).text =
                                            hour.wind_dir.toString()

                                        popview.findViewById<TextView>(R.id.dlg_city_cloud).text =
                                            hour.cloud.toString() + " oktas"

                                        popview.findViewById<TextView>(R.id.dlg_city_uv_index).text =
                                            hour.uv.toString()

                                        popview.findViewById<TextView>(R.id.dlg_city_precip).text =
                                            hour.precip_mm.toString() + "mm"

                                        popview.findViewById<TextView>(R.id.dlg_city_visibility).text =
                                            hour.vis_km.toString() + " km"

                                        popview.findViewById<TextView>(R.id.dlg_city_gust).text =
                                            hour.gust_kph.toString()

                                        popview.findViewById<TextView>(R.id.dlg_city_wind_degree).text =
                                            hour.wind_degree.toString()

                                        popview.findViewById<TextView>(R.id.dlg_city_heat_ind).text =
                                            hour.heatindex_c.toString()
                                        val input = SimpleDateFormat(
                                            "yyyy-MM-dd hh:mm", Locale.getDefault()
                                        )
                                        val output =
                                            SimpleDateFormat("hh:mm aa", Locale.getDefault())
                                        val date_output =
                                            SimpleDateFormat("dd/MM/YY", Locale.getDefault())
                                        try {
                                            var t: Date = input.parse(hour.time)
                                            popview.findViewById<TextView>(R.id.dlg_city_close_time).text =
                                                date_output.format(t)
                                                    .toString() + " " + output.format(t).toString()
                                        } catch (e: Exception) {
                                            //No Code
                                        }

                                        if (hour.will_it_rain == 1) popview.findViewById<TextView>(R.id.dlg_will_it_rain).text =
                                            "Yes"
                                        else popview.findViewById<TextView>(R.id.dlg_will_it_rain).text =
                                            "No"
                                        if (hour.will_it_snow == 1) popview.findViewById<TextView>(R.id.dlg_will_it_snow).text =
                                            "Yes"
                                        else popview.findViewById<TextView>(R.id.dlg_will_it_snow).text =
                                            "No"
                                        val popdialog = builder.create()
                                        popdialog.show()

                                        val close: ImageView =
                                            popview.findViewById(R.id.dlg_city_close)
                                        close.setOnClickListener {
                                            popdialog.dismiss()
                                        }
                                    }
                                })

                            var recyclerView: RecyclerView
                            recyclerView = findViewById(R.id.detail_city_recyclerview)
                            recyclerView.adapter = dailyForecastAdapter
                        }

                        //add tomorrow's forecast
                        if (!(tom_mainList.isEmpty())) {
                            val dailyForecastAdapter = DailyForecastAdapter(tom_mainList,
                                object : DailyForecastAdapter.ItemClickListener {
                                    override fun onItemClick(hour: Hour) {

                                        val builder =
                                            androidx.appcompat.app.AlertDialog.Builder(this@DetailActivity)
                                        val popview: View =
                                            layoutInflater.inflate(R.layout.dialouge_box, null)
                                        builder.setView(popview)

                                        popview.findViewById<TextView>(R.id.dlg_city_Temp).text =
                                            hour.temp_c.toString() + "°C"

                                        var code = hour.condition.icon.subSequence(41, 44)
                                            .toString()
                                        var icon = getIcon(code)
                                        popview.findViewById<ImageView>(R.id.dlg_city_curr_img)
                                            .setImageResource(icon)

                                        popview.findViewById<TextView>(R.id.dlg_city_condition).text =
                                            hour.condition.text.toString()

                                        popview.findViewById<TextView>(R.id.dlg_city_wind_speed).text =
                                            hour.wind_kph.toString() + " km/h"

                                        popview.findViewById<TextView>(R.id.dlg_city_air_preassure).text =
                                            hour.pressure_mb.toString() + " mb"

                                        popview.findViewById<TextView>(R.id.dlg_city_humidity).text =
                                            hour.humidity.toString() + " g.m-3"

                                        popview.findViewById<TextView>(R.id.dlg_city_wind_direction).text =
                                            hour.wind_dir.toString()

                                        popview.findViewById<TextView>(R.id.dlg_city_cloud).text =
                                            hour.cloud.toString() + " oktas"

                                        popview.findViewById<TextView>(R.id.dlg_city_uv_index).text =
                                            hour.uv.toString()

                                        popview.findViewById<TextView>(R.id.dlg_city_precip).text =
                                            hour.precip_mm.toString() + "mm"

                                        popview.findViewById<TextView>(R.id.dlg_city_visibility).text =
                                            hour.vis_km.toString() + " km"

                                        popview.findViewById<TextView>(R.id.dlg_city_gust).text =
                                            hour.gust_kph.toString()

                                        popview.findViewById<TextView>(R.id.dlg_city_wind_degree).text =
                                            hour.wind_degree.toString()

                                        popview.findViewById<TextView>(R.id.dlg_city_heat_ind).text =
                                            hour.heatindex_c.toString()
                                        val input = SimpleDateFormat(
                                            "yyyy-MM-dd hh:mm", Locale.getDefault()
                                        )
                                        val output =
                                            SimpleDateFormat("hh:mm aa", Locale.getDefault())
                                        val date_output =
                                            SimpleDateFormat("dd/MM/YY", Locale.getDefault())
                                        try {
                                            var t: Date = input.parse(hour.time)
                                            popview.findViewById<TextView>(R.id.dlg_city_close_time).text =
                                                date_output.format(t)
                                                    .toString() + " " + output.format(t).toString()
                                        } catch (e: Exception) {
                                            //No Code
                                        }

                                        if (hour.will_it_rain == 1) popview.findViewById<TextView>(R.id.dlg_will_it_rain).text =
                                            "Yes"
                                        else popview.findViewById<TextView>(R.id.dlg_will_it_rain).text =
                                            "No"
                                        if (hour.will_it_snow == 1) popview.findViewById<TextView>(R.id.dlg_will_it_snow).text =
                                            "Yes"
                                        else popview.findViewById<TextView>(R.id.dlg_will_it_snow).text =
                                            "No"
                                        val popdialog = builder.create()
                                        popdialog.show()

                                        val close: ImageView =
                                            popview.findViewById(R.id.dlg_city_close)
                                        close.setOnClickListener {
                                            popdialog.dismiss()
                                        }
                                    }
                                })

                            var recyclerView: RecyclerView
                            recyclerView = findViewById(R.id.detail_city_tomm_recyclerview)
                            recyclerView.adapter = dailyForecastAdapter
                        }

                        //add Over-tomorrow's forecast
                        if (!(ovtom_mainList.isEmpty())) {
                            val dailyForecastAdapter = DailyForecastAdapter(ovtom_mainList,
                                object : DailyForecastAdapter.ItemClickListener {
                                    override fun onItemClick(hour: Hour) {

                                        val builder =
                                            androidx.appcompat.app.AlertDialog.Builder(this@DetailActivity)
                                        val popview: View =
                                            layoutInflater.inflate(R.layout.dialouge_box, null)
                                        builder.setView(popview)

                                        popview.findViewById<TextView>(R.id.dlg_city_Temp).text =
                                            hour.temp_c.toString() + "°C"

                                        var code = hour.condition.icon.subSequence(41, 44)
                                            .toString()
                                        var icon = getIcon(code)
                                        popview.findViewById<ImageView>(R.id.dlg_city_curr_img)
                                            .setImageResource(icon)

                                        popview.findViewById<TextView>(R.id.dlg_city_condition).text =
                                            hour.condition.text.toString()

                                        popview.findViewById<TextView>(R.id.dlg_city_wind_speed).text =
                                            hour.wind_kph.toString() + " km/h"

                                        popview.findViewById<TextView>(R.id.dlg_city_air_preassure).text =
                                            hour.pressure_mb.toString() + " mb"

                                        popview.findViewById<TextView>(R.id.dlg_city_humidity).text =
                                            hour.humidity.toString() + " g.m-3"

                                        popview.findViewById<TextView>(R.id.dlg_city_wind_direction).text =
                                            hour.wind_dir.toString()

                                        popview.findViewById<TextView>(R.id.dlg_city_cloud).text =
                                            hour.cloud.toString() + " oktas"

                                        popview.findViewById<TextView>(R.id.dlg_city_uv_index).text =
                                            hour.uv.toString()

                                        popview.findViewById<TextView>(R.id.dlg_city_precip).text =
                                            hour.precip_mm.toString() + "mm"

                                        popview.findViewById<TextView>(R.id.dlg_city_visibility).text =
                                            hour.vis_km.toString() + " km"

                                        popview.findViewById<TextView>(R.id.dlg_city_gust).text =
                                            hour.gust_kph.toString()

                                        popview.findViewById<TextView>(R.id.dlg_city_wind_degree).text =
                                            hour.wind_degree.toString()

                                        popview.findViewById<TextView>(R.id.dlg_city_heat_ind).text =
                                            hour.heatindex_c.toString()
                                        val input = SimpleDateFormat(
                                            "yyyy-MM-dd hh:mm", Locale.getDefault()
                                        )
                                        val output =
                                            SimpleDateFormat("hh:mm aa", Locale.getDefault())
                                        val date_output =
                                            SimpleDateFormat("dd/MM/YY", Locale.getDefault())
                                        try {
                                            var t: Date = input.parse(hour.time)
                                            popview.findViewById<TextView>(R.id.dlg_city_close_time).text =
                                                date_output.format(t)
                                                    .toString() + " " + output.format(t).toString()
                                        } catch (e: Exception) {
                                            //No Code
                                        }

                                        if (hour.will_it_rain == 1) popview.findViewById<TextView>(R.id.dlg_will_it_rain).text =
                                            "Yes"
                                        else popview.findViewById<TextView>(R.id.dlg_will_it_rain).text =
                                            "No"
                                        if (hour.will_it_snow == 1) popview.findViewById<TextView>(R.id.dlg_will_it_snow).text =
                                            "Yes"
                                        else popview.findViewById<TextView>(R.id.dlg_will_it_snow).text =
                                            "No"
                                        val popdialog = builder.create()
                                        popdialog.show()

                                        val close: ImageView =
                                            popview.findViewById(R.id.dlg_city_close)
                                        close.setOnClickListener {
                                            popdialog.dismiss()
                                        }
                                    }
                                })

                            var recyclerView: RecyclerView
                            recyclerView = findViewById(R.id.detail_city_ovtomm_recyclerview)
                            recyclerView.adapter = dailyForecastAdapter
                        }

                    } else {
                        Toast.makeText(this@DetailActivity, "Response Failure", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<ForecastDetails>, t: Throwable) {
                    Toast.makeText(this@DetailActivity, "Network Failure", Toast.LENGTH_SHORT)
                        .show()
                }

            })
    }

    private fun getIcon(code: String): Int {
        val map = when (code) {
            "113" -> R.drawable.ic_sunny
            "338" -> R.drawable.ic_sunnycloudy
            "350" -> R.drawable.ic_sunnycloudy
            "368" -> R.drawable.ic_sunnycloudy
            "371" -> R.drawable.ic_snowy
            "227" -> R.drawable.ic_snowy
            "374" -> R.drawable.ic_snowy
            "377" -> R.drawable.ic_snowy
            "386" -> R.drawable.ic_rainythunder
            "389" -> R.drawable.ic_rainythunder
            "392" -> R.drawable.ic_rainythunder
            "395" -> R.drawable.ic_rainythunder
            "116" -> R.drawable.ic_sunnycloudy
            "179" -> R.drawable.ic_sunnycloudy
            "323" -> R.drawable.ic_sunnycloudy
            "335" -> R.drawable.ic_sunnycloudy
            "329" -> R.drawable.ic_sunnycloudy
            "119" -> R.drawable.ic_cloudy
            "185" -> R.drawable.ic_cloudy
            "326" -> R.drawable.ic_cloudy
            "332" -> R.drawable.ic_cloudy
            "122" -> R.drawable.ic_cloudy
            "143" -> R.drawable.ic_cloudy
            "200" -> R.drawable.ic_thunder
            "299" -> R.drawable.ic_sunnyrainy
            "305" -> R.drawable.ic_sunnyrainy
            "293" -> R.drawable.ic_sunnyrainy
            "353" -> R.drawable.ic_sunnyrainy
            "365" -> R.drawable.ic_sunnyrainy
            "356" -> R.drawable.ic_sunnyrainy
            "359" -> R.drawable.ic_sunnyrainy
            "176" -> R.drawable.ic_sunnyrainy
            "362" -> R.drawable.ic_sunnyrainy
            "377" -> R.drawable.ic_sunnyrainy
            "182" -> R.drawable.ic_sunnyrainy
            "248" -> R.drawable.ic_pressure
            "260" -> R.drawable.ic_pressure
            "230" -> R.drawable.ic_pressure
            "311" -> R.drawable.ic_windy
            "314" -> R.drawable.ic_windy
            "263" -> R.drawable.ic_rainy
            "296" -> R.drawable.ic_rainy
            "302" -> R.drawable.ic_rainy
            "317" -> R.drawable.ic_rainy
            "320" -> R.drawable.ic_rainy
            "308" -> R.drawable.ic_rainy
            "266" -> R.drawable.ic_rainy
            "281" -> R.drawable.ic_rainy
            "284" -> R.drawable.ic_rainy
            else -> R.drawable.ic_sunnycloudy
        }
        return map
    }
}