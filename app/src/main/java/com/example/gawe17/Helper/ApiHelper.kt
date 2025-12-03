package com.example.gawe17.Helper

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object ApiHelper {

    val baseApi = "http://192.168.1.16:5000/api/"
    fun post(endpoint: String, jsonBody: JSONObject): Pair<Int, String?>{
        return try {
            val url = URL(baseApi+endpoint)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput=true

            conn.outputStream.use { it.write(jsonBody.toString().toByteArray()) }

            val responseCode = conn.responseCode
            val responseText = try{
                if (responseCode in 200..299){
                    conn.inputStream.bufferedReader().use { it.readText() }
                }else{
                    conn.errorStream?.bufferedReader()?.use { it.readText() }
                }
            }catch (e: Exception){null}
            Pair(responseCode, responseText)
        }catch (e: Exception){
            e.printStackTrace()
            Pair(-1, null)
        }
    }
}