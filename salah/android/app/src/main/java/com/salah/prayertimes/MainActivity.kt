package com.salah.prayertimes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.androidbrowserhelper.trusted.TwaLauncher

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Launch TWA pointing to your PWA
        val twaLauncher = TwaLauncher(this)
        val url = "https://salah.aqib.cloud"
        twaLauncher.launch(
            uri = Uri.parse(url),
            activity = this,
            splashScreenStrategy = null,
            keepAlive = true
        )
        
        // Finish this activity since TWA takes over
        finish()
    }
}
