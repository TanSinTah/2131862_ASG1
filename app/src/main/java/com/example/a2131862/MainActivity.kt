package com.example.a2131862

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
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
        binding.btnGoToSecondActivity.setOnClickListener {
            //Create an Intent to navigate to the second activity
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }


        val items = arrayOf("No Of Comment", "Sentiment", "Sentiment Score", "Ticker")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = items[position]
                // Do something with the selected item
                Toast.makeText(this@MainActivity, "Selected: $selectedItem", Toast.LENGTH_SHORT)
                    .show()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle nothing selected event
            }
        }
    }

    private fun parseJson(jsonArray: JSONArray): String {
        val resultStringBuilder = StringBuilder()
        try {
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val noOfComments = jsonObject.optInt("no_of_comments", -1)
                val sentiment = jsonObject.optString("sentiment", "Unknown")
                val sentimentScore = jsonObject.optDouble("sentiment_score", 0.0)
                val ticker = jsonObject.optString("ticker", "Unknown")

                // Build a result string with the parsed data
                resultStringBuilder.append("Ticker: $ticker, No. of Comments: $noOfComments, Sentiment: $sentiment, Score: $sentimentScore\n")
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            return "JSON parsing error"
        }

        return resultStringBuilder.toString()
    }

    private fun handleRetrieveQuoteWithVolley() {
        val queue = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, "https://tradestie.com/api/v1/apps/reddit", null,
            { response ->
                if (response.length() > 0) {
                    try {
                        val quote = parseJson(response)
                        binding.textView.text = quote
                    } catch (e: JSONException) {
                        handleError("JSON parsing error")
                    }
                } else {
                    handleError("No data found")
                }
            },
            { error ->
                val errorMessage = "Error: ${error.message}"
                handleError(errorMessage)
            }
        )
        queue.add(jsonArrayRequest)
    }

    private fun handleNetworkResponse(response: JSONArray) {
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
    }

    private fun handleNetworkError(error: Exception) {
        Log.e("VolleyError", "Error: ${error.message}")
        binding.textView.text = "That didn't work! Error: ${error.message}"
    }
    private fun handleError(errorMessage: String) {
        Log.e("VolleyError", errorMessage)
        binding.textView.text = "Something went wrong: $errorMessage"
    }
}