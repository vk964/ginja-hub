package com.ginjahub.app

import android.app.Application
import com.ginjahub.app.launch.AppsFlyerHandoff

class GinjaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppsFlyerHandoff.initialize(this)
    }
}
