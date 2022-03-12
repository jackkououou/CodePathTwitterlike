package com.codepath.apps.restclienttemplate

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.apps.restclienttemplate.models.Tweet
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val SECOND_MILLIS = 1000
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS

class TweetsAdapter(val tweets: ArrayList<Tweet>) : RecyclerView.Adapter<TweetsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.item_tweet, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tweet: Tweet = tweets.get(position)


        holder.tvUsername.text = tweet.user?.name
        holder.tvTweetBody.text = tweet.body
        holder.tvAge.text = getRelativeTimeAgo(tweet.createdAt)
        Glide.with(holder.itemView).load(tweet.user?.publicImageUrl).into(holder.ivProfileImage)
    }

    override fun getItemCount(): Int {
        return tweets.size
    }

    // Clean all elements of the recycler
    fun clear() {
        tweets.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(tweetList: List<Tweet>) {
        tweets.addAll(tweetList)
        notifyDataSetChanged()
    }

    private fun getRelativeTimeAgo(rawJsonDate: String): String {
        val twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy"
        val glyph: Long
        val sf = SimpleDateFormat(twitterFormat, Locale.ENGLISH)
        sf.isLenient = true
        try {
            val time: Long = sf.parse(rawJsonDate).time
            val now = System.currentTimeMillis()
            val diff = now - time
            if (diff < MINUTE_MILLIS) {
                return "just now"
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago"
            } else if (diff < 50 * MINUTE_MILLIS) {
                glyph = diff / MINUTE_MILLIS
                return "$glyph m"
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago"
            } else if (diff < 24 * HOUR_MILLIS) {
                glyph = diff / HOUR_MILLIS
                return "$glyph h"
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday"
            } else {
                glyph = diff / DAY_MILLIS
                return "$glyph d"
            }
        } catch (e: ParseException) {
            Log.i(TAG, "getRelativeTimeAgo failed")
            e.printStackTrace()
        }
        return ""
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val ivProfileImage = itemView.findViewById<ImageView>(R.id.ivProfileImage)
        val tvUsername = itemView.findViewById<TextView>(R.id.tvUsername)
        val tvTweetBody = itemView.findViewById<TextView>(R.id.tvTweetBody)
        val tvAge = itemView.findViewById<TextView>(R.id.tvAge)

    }

    companion object {
        private const val TAG = "TweetsAdapter"
    }

}