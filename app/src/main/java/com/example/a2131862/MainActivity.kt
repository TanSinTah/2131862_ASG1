package com.example.a2131862

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.a2131862.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONException

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnQuote.setOnClickListener {
            handleRetrieveQuoteWithVolley()
        }

        val spinner = findViewById<Spinner>(R.id.spinner)
        val items = arrayOf("no_of_comments", "sentiment", "sentiment_score", "ticker")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = items[position]
                // Do something with the selected item
                Toast.makeText(this@MainActivity, "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle nothing selected event
            }
        }
    }

    private fun parseJson(jsonArray: JSONArray): String {
        try {
            if (jsonArray.length() > 0) {
                val jsonObject = jsonArray.getJSONObject(0) // Assuming you want the first object
                val sentiment = jsonObject.getString("sentiment")
                return "Sentiment: $sentiment"
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return "Parsing error"
    }

    private fun handleRetrieveQuoteWithVolley() {
        val queue = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, "https://tradestie.com/api/v1/apps/reddit", null,
            { response ->
                Log.d("VolleyResponse", "Response: $response")
                if (response.length() > 0) {
                    try {
                        val quote = parseJson(response)
                        binding.textView.text = quote
                    } catch (e: JSONException) {
                        binding.textView.text = "JSON parsing error"
                    }
                } else {
                    binding.textView.text = "No data found"
                }
            },
            { error ->
                Log.e("VolleyError", "Error: ${error.message}")
                binding.textView.text = "That didn't work! Error: ${error.message}"
            }
        )
        queue.add(jsonArrayRequest)
    }
}
//    private fun handleRetrieveQuoteWithVolley(){
//
//        val queue = Volley.newRequestQueue(this)
//
//        val jsonArrayRequest = JsonArrayRequest(
//
//            Request.Method.GET, "https://tradestie.com/api/v1/apps/reddit", null, { response ->
//
//
//                for (i in 0 until response.length()) {
//                    val jsonObject = response.getJSONObject(i)
//
//                    //no of comments
//                    val no_of_comments = jsonObject.getString("no_of_comments")
//                    Log.i("no_of_comments:", no_of_comments)
//                    binding.textView.text = no_of_comments
//
//                    //sentiment
//                    val sentiments = jsonObject.getString("sentiment")
//                    Log.i("sentiment: ", sentiments)
//
//                    //sentiment score
//                    val sentiment_score = jsonObject.getString("sentiment_score")
//                    Log.i("sentiment_score: ", sentiment_score)
//
//                    //ticker
//                    val ticker = jsonObject.getString("ticker")
//                    Log.i("ticker: ", ticker)
//                }
//            },
//            {error -> binding.textView.text = "That didn't work!:  $error" }
//        )
//        queue.add(jsonArrayRequest)
//    }
//    fun parseJson(jsonString: String) {
//        val jsonArray = JSONObject(jsonString)
//
//        for(i in 0 until jsonArray.length()) {
//
//            val jsonObject = jsonArray.getJSONObject(i.toString())
//
//            val noOfComments = jsonObject.getInt("no_of_comments")
//            val sentiment = jsonObject.getString("sentiment")
//            val sentimentScore = jsonObject.getDouble("sentiment_score")
//            val ticker = jsonObject.getString("ticker")
//
//            // Do something with the parsed data, e.g., print or store in a data structure
//            println("Ticker: $ticker, No. of Comments: $noOfComments, Sentiment: $sentiment, Score: $sentimentScore")
//        }
//        Log.d("Modified JSON", jsonArray.toString())
//    }
//}