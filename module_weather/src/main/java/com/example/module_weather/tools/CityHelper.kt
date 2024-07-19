package com.example.module_weather.tools



import com.example.module_weather.data.CityInfo
import com.google.gson.Gson
import java.io.File

object CityHelper {
    fun initHeaderCity():CityInfo{
        val jsonPath = "assets///city.json"
        var text = File(jsonPath).readText()
        var gson = Gson()
        var json = gson.fromJson<CityInfo>(text, CityInfo::class.java)
//        json.forEach { city ->
//            city.districts
//                .forEach {
//                    district ->
//                    map[district.adcode] = district.name
//                }
//        }
        return json
    }
}