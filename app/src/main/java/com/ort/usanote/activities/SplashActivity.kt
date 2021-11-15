package com.ort.usanote.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.transition.Slide
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import com.ort.usanote.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window){
            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
            this.exitTransition = Slide()
        }

        setContentView(R.layout.activity_splash)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        val text = findViewById<TextView>(R.id.tv_app_name)
        text.typeface = Typeface.createFromAsset(assets,"fonts/Quantum.otf")
        Handler().postDelayed(
            {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()

            },3500)

    }
}