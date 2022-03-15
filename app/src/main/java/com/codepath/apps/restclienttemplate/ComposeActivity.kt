package com.codepath.apps.restclienttemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var buttonTweet: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etCompose)
        buttonTweet = findViewById(R.id.buttonTweet)


        buttonTweet.setOnClickListener {

            //Grabbing content of eddit text
            //Then checking if it is not empty
            //checking if it under char limit
            //making api call to post
            val tweetContent = etCompose.text.toString()


        }
    }
}