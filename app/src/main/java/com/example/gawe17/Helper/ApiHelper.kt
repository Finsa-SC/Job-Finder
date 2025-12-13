package com.example.gawe17.Helper

import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

object ApiHelper {
    private const val baseUrl = "http://10.0.2.2:5000/api/Controller/"

    private fun request(method: String, endpoint: String, jsonBody: JSONObject? = null): Pair<Int, String?> {
        var conn: HttpURLConnection? = null
        return try{
            conn = (URL(baseUrl+endpoint).openConnection() as HttpURLConnection).apply {
                requestMethod=method
                connectTimeout = 5000
                readTimeout = 5000
                if(jsonBody!=null){
                    setRequestProperty("Content-Type", "application/json")
                    doOutput=true
                }
            }
            jsonBody?.let{
                conn.outputStream.use { outputStream ->
                    outputStream.write( it.toString().toByteArray())
                }
            }

            val responseCode = conn.responseCode

            val responseText = try{
                if(responseCode in 200..299){
                    conn.inputStream.bufferedReader().use { it.readText() }
                }else{
                    conn.errorStream?.bufferedReader()?.use { it.readText() }
                }
                }catch (e:Exception){
                    e.printStackTrace()
                    null
                }

            Pair(responseCode, responseText)
        }
        catch (e: IOException){
            e.printStackTrace()
            Pair(-1, null)
        }
        catch (e:Exception){
            e.printStackTrace()
            Pair(-2, null)
        }
        finally {
            conn?.disconnect()
        }
    }

    fun post(endpoint: String, jsonObject: JSONObject)
        = request("POST", endpoint, jsonObject)
    fun put(endpoint: String, jsonObject: JSONObject)
        = request("PUT", endpoint, jsonObject)
    fun get(endpoint: String)
        = request("GET", endpoint)
    fun delete(endpoint: String)
        = request("DELETE", endpoint)
}