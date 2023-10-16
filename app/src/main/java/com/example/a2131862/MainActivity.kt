package com.example.a2131862

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.a2131862.databinding.ActivityMainBinding
import org.json.JSONObject

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
        val quote = jsonObject.getString("value")
        return quote
    }
    private fun handleRetrieveQuoteWithVolley(){
        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, "https://tradestie.com/api/v1/apps/reddit", null, {
                    response -> binding.textView.text= parseJson(response.toString()) },
            {binding.textView.text = "That didn't work!" + ": $it"}
        )
        queue.add(jsonObjectRequest)
    }
}
