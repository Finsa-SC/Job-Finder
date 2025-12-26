package com.example.gawe17.core.network

import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

object ApiHelper {
    private const val baseUrl = "http://10.0.2.2:5000/api/"

    private fun request(method: String, endpoint: String, jsonBody: JSONObject? = null): Pair<Int, String?>{
        var conn: HttpURLConnection? = null
        return try{
            conn = (URL(baseUrl+endpoint).openConnection() as HttpURLConnection).apply {
                requestMethod= method
                connectTimeout=5000
                readTimeout=5000
                if(jsonBody!=null){
                    setRequestProperty("Content-Type", "application/json")
                    doOutput=true
                }
            }

            jsonBody?.let {
                conn.outputStream.use { os ->
                    os.write(it.toString().toByteArray())
                }
            }

            val responseCode = conn.responseCode

            val responseText = try{
                if(responseCode in 200..299){
                    conn.inputStream.bufferedReader().use { it.readText() }
                }
                else{
                    conn.errorStream?.bufferedReader()?.use { it.readText() }
                }
            }catch (e:Exception){null}
            Pair(responseCode, responseText)
        }
        catch (e: IOException){
            e.printStackTrace()
            Pair(-1, null)
        }
        catch (e: Exception){
            e.printStackTrace()
            Pair(-2, null)
        }
    }

    fun post(endpoint: String, jsonBody: JSONObject? = null): Pair<Int, String?> = request("POST", endpoint, jsonBody)
    fun put(endpoint: String, jsonBody: JSONObject? = null): Pair<Int, String?> = request("PUT", endpoint, jsonBody)
    fun get(endpoint: String): Pair<Int, String?> = request("GET", endpoint)
    fun delete(endpoint: String): Pair<Int, String?> = request("DELETE", endpoint)
}