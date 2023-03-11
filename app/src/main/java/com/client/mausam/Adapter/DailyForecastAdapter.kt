package com.client.mausam.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.client.mausam.Models.Hour
import com.client.mausam.R
import java.text.SimpleDateFormat
import java.util.*

class DailyForecastAdapter(
    private val dailyForecastList: List<Hour>,
    val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<DailyForecastAdapter.ViewHolder>() {
    interface ItemClickListener {
        fun onItemClick(hour: Hour)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.daily_forcast_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dailyForecastList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dailyForecastList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //        var daily_forecast_item_date =
//            itemView.findViewById<TextView>(R.id.daily_forecast_item_date)
        var daily_forecast_item_time =
            itemView.findViewById<TextView>(R.id.daily_forecast_item_time)
        var daily_forecast_item_temp =
            itemView.findViewById<TextView>(R.id.daily_forecast_item_temp)
        var daily_forecast_item_img = itemView.findViewById<ImageView>(R.id.daily_forecast_item_img)
        var daily_forecast_item_condition =
            itemView.findViewById<TextView>(R.id.daily_forecast_item_condition)

        fun bind(hour: Hour) {
            daily_forecast_item_temp.text = hour.temp_c.toString() + "°C"
            daily_forecast_item_condition.text = hour.condition.text

            //-> get image code from url
            var code = hour.condition.icon.subSequence(41, 44).toString()
            var icon = getIcon(code)
            daily_forecast_item_img.setImageResource(icon)

            //-> set time
            val input = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault())
            val output = SimpleDateFormat("hh:mm aa", Locale.getDefault())
//            val date_output = SimpleDateFormat("dd/MM/YY", Locale.getDefault())
            try {
                var t: Date = input.parse(hour.time)
                daily_forecast_item_time.text = output.format(t)
//                daily_forecast_item_date.text = date_output.format(t)
            } catch (e: Exception) {
                //No Code
            }
            itemView.setOnClickListener {
                itemClickListener.onItemClick(hour)
            }
        }
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