package com.client.mausam.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.client.mausam.Models.SearchList
import com.client.mausam.Models.SearchListItem
import com.client.mausam.R

class SearchCityAdapter(
    private val cityList: List<SearchListItem>, val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<SearchCityAdapter.ViewHolder>() {

    interface ItemClickListener {
        fun onItemClick(city: SearchListItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_search_city, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cityList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cityName = itemView.findViewById<TextView>(R.id.city_name)
        var cityRegion = itemView.findViewById<TextView>(R.id.city_region)
        var cityCountry = itemView.findViewById<TextView>(R.id.city_country)
        var citylat = itemView.findViewById<TextView>(R.id.city_lat)
        var citylon = itemView.findViewById<TextView>(R.id.city_lon)

        fun bind(city: SearchListItem) {
            cityName.text = city.name.toString()
            cityRegion.text = city.region.toString()
            cityCountry.text = city.country.toString()
            citylat.text = city.lat.toString()
            citylon.text = city.lon.toString()
            itemView.setOnClickListener {
                itemClickListener.onItemClick(city)
            }
        }
    }

}