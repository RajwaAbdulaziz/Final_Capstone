package com.tuwaiq.finalcapstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.widget.VideoView
import pl.droidsonroids.gif.GifImageView

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var gif: GifImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        gif = findViewById(R.id.gif)
        gif.alpha = 0f
        gif.animate().setDuration(4000).alpha(1f).withEndAction{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}