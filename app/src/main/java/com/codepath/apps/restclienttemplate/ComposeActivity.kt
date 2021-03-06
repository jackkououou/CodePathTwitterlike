package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.android.parcel.Parcelize
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    private lateinit var etCompose: EditText
    lateinit var buttonTweet: Button
    private lateinit var tvCharCount: TextView
    private lateinit var client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etCompose)
        buttonTweet = findViewById(R.id.buttonTweet)
        tvCharCount = findViewById(R.id.tvCharCount)
        client = TwitterApplication.getRestClient(this)

        etCompose.addTextChangedListener(object: TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var tweetCharRemaining = 280 - etCompose.text.toString().length

                when {
                    tweetCharRemaining >= 0 -> {
                        "$tweetCharRemaining / 280".also { tvCharCount.text = it }
                    }
                    else -> {
                        "Tweet is too long.".also { tvCharCount.text = it }
                    }
                }

            }


        })

        buttonTweet.setOnClickListener {

            //Grabbing content of edit text
            //Then checking if it is not empty
            //checking if it under char limit
            //making api call to post
            val tweetContent = etCompose.text.toString()

            when {
                tweetContent.isEmpty() -> {
                    Toast.makeText(this, "Empty tweets not allowed!",
                        Toast.LENGTH_SHORT).show()
                }
                tweetContent.length > 280 -> {
                    Toast.makeText(this, "Tweet is too long! Character limit is 280",
                        Toast.LENGTH_SHORT).show()
                }
                else -> {
                    client.publishTweet(tweetContent, object: JsonHttpResponseHandler() {
                        override fun onFailure(
                            statusCode: Int,
                            headers: Headers?,
                            response: String?,
                            throwable: Throwable?
                        ) {
                            Log.e(TAG, "onFailure! $statusCode, response $response", throwable)
                        }

                        override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                            Log.i(TAG, "onSuccess! $statusCode")

                            val tweet: Tweet = Tweet.fromJson(json.jsonObject)

                            val intent = Intent()
                            intent.putExtra("tweet", tweet)
                            setResult(RESULT_OK, intent)
                            finish()
                        }

                    })
                }
            }
        }


    }

    companion object {
        const val TAG : String = "ComposeActivity"
    }
}