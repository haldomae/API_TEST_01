package com.classnumber_00_domaekazuki.api_test_01

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    // 画面から取得した部品が入る
    private lateinit var nameTextView: TextView
    private lateinit var pokemonImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // 画面から部品を見つける
        nameTextView = findViewById(R.id.nameTextView)
        pokemonImageView = findViewById(R.id.pokemonImageView)
        Thread {
            try {
                val url = URL("https://pokeapi.co/api/v2/pokemon/mewtwo")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use {
                        it.readText()
                    }
                    val json = JSONObject(response)
                    val name = json.getString("name")
                    val imageUrl = json.getJSONObject("sprites").getString("front_default")
                    val imageURL = URL(imageUrl)
                    val imageConnection = imageURL.openConnection() as HttpURLConnection
                    val imageStream = imageConnection.inputStream
                    val bitmap = BitmapFactory.decodeStream(imageStream)

                    runOnUiThread {
                        nameTextView.text = name
                        pokemonImageView.setImageBitmap(bitmap)
                    }
                } else {
                    Log.e("ERROR", "HTTP error code: $responseCode")
                }
            } catch (e: Exception) {
                Log.e("ERROR", "Exception occurred", e)
            }
        }.start()

    }
}