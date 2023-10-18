package com.example.a2131862

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.a2131862.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnQuote.setOnClickListener {
            handleRetrieveQuoteWithVolley()
        }
    }
    private fun parseJson(jsonData: String?): String {
        val jsonObject = JSONObject(jsonData)
        val quote = jsonObject.getString("name")
        return quote
    }
    private fun handleRetrieveQuoteWithVolley(){

        val queue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(

            Request.Method.GET, "https://tradestie.com/api/v1/apps/reddit", null, { response ->

                val jsonArray = JSONTokener(response.toString()).nextValue() as JSONArray
                for (i in 0 until jsonArray.length()) {

                    //no of comments
                    val no_of_comments = jsonArray.getJSONObject(i).getString("no_of_comments")
                    Log.i("no_of_comments:", no_of_comments)

                    //sentiment
                    val sentiments = jsonArray.getJSONObject(i).getString("sentiment")
                    Log.i("sentiment: ", sentiments)

                    //sentiment score
                    val sentiment_score = jsonArray.getJSONObject(i).getString("sentiment_score")
                    Log.i("sentiment_score: ", sentiment_score)

                    //ticker
                    val ticker = jsonArray.getJSONObject(i).getString("ticker")
                    Log.i("ticker: ", ticker)

                }
            },
            {error -> binding.textView.text = "That didn't work!: $error" }
        )
        queue.add(jsonObjectRequest)
    }
    }