package com.example.android.memeshareapp

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var currentImageUrl :String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       // val shareButton : Button = findViewById(R.id.shareButton)
        //val nextButton : Button = findViewById(R.id.nextButton)

        //shareButton.isEnabled = true

        // loads a new meme every time the activity is created : that is app launched.
        loadMeme()

        shareButton.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "share button clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(Intent.ACTION_SEND);
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT,"Hey Check out this cool meme I got from Reddit $currentImageUrl")
            val choser = Intent.createChooser(intent, "Share this meme using......")
            startActivity(choser)
        })

        nextButton.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "next button clicked ", Toast.LENGTH_SHORT).show()
            //loading another meme
            loadMeme()
        })
    }

    private fun loadMeme(){

        val progressBar:ProgressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        val url = "https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    currentImageUrl = response.getString("url")
                    val memeImageView :ImageView = findViewById(R.id.memeImageView)
                    //using a listener for glide to check when the image is loaded.
                    Glide.with(this).load(currentImageUrl).listener(object: RequestListener<Drawable>{
                        override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }

                    }).into(memeImageView)
                },
                { error ->
                    Toast.makeText(this, "error in api call:${error.message}", Toast.LENGTH_SHORT).show()
                })

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

}
