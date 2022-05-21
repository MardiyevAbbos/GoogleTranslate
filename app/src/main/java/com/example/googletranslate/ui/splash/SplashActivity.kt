package com.example.googletranslate.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.googletranslate.MainActivity
import com.example.googletranslate.R
import com.example.googletranslate.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    private lateinit var topAnim: Animation
    private lateinit var bottomAnim: Animation
    private val SPLASH_SCREEN = 2400

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        initViews()

    }

    private fun initViews() {
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        binding.ivWorld.animation = topAnim
        binding.ivGoogle.animation = bottomAnim
        binding.ivTranslation.animation = bottomAnim

        postDelay()
    }

    private fun postDelay(){
        Handler().postDelayed(Runnable {
            val intent = Intent(this, MainActivity::class.java)
            //
            startActivity(intent)
            finish()
        }, SPLASH_SCREEN.toLong())
    }

}