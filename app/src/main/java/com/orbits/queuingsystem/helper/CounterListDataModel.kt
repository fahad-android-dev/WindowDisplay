package com.orbits.queuingsystem.helper

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

data class CounterListDataModel(
    val id: String ?= null,
    val name: String ?= null,
    val counterType: String ?= null,
    val serviceId: String ?= null,
    val tokenStart: Int ?= 0,
    var isSelected: Boolean ?= false,
    val tokenEnd: Int ?= 0,
):java.io.Serializable


fun parseJsonData(jsonObject: JsonObject): List<CounterListDataModel> {
    val itemsList = mutableListOf<CounterListDataModel>()
    val itemsArray: JsonArray = jsonObject.getAsJsonArray("counters")

    for (element: JsonElement in itemsArray) {
        val itemObject = element.asJsonObject
        val item = Gson().fromJson(itemObject, CounterListDataModel::class.java)
        itemsList.add(item)
    }

    return itemsList
}
