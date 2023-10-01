package com.bbbrbetss.comebbrabettt.app

import android.app.Application
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.bbbrbetss.comebbrabettt.app.di.AppComponent
import com.bbbrbetss.comebbrabettt.app.di.DaggerAppComponent
import com.onesignal.OneSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class App : Application() {
    val component: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.Main).launch {
            OneSignal.initWithContext(applicationContext, ONE_SIGNAL_ID)
            OneSignal.Notifications.requestPermission(true)
        }
        Firebase.remoteConfig.apply {
            setConfigSettingsAsync(
                remoteConfigSettings {
                    minimumFetchIntervalInSeconds = MIN_FETCH_CONFIGS_INTERVAL
                }
            )
            setDefaultsAsync(R.xml.remote_config_defaults)
            addOnConfigUpdateListener(
                object : ConfigUpdateListener {
                    override fun onUpdate(configUpdate: ConfigUpdate) {
                        activate()
                    }

                    override fun onError(error: FirebaseRemoteConfigException) {
                        Log.e(this@App::class.java.simpleName, "Config update error", error)
                    }
                }
            )
        }
    }

    private companion object {
        const val MIN_FETCH_CONFIGS_INTERVAL = 3600L
        const val ONE_SIGNAL_ID = "3f87b7f6-10d5-48cf-bf7b-006c18bb12c9"
    }
}
