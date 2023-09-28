package com.missapps.brabet

import android.app.Application
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.missapps.brabet.di.AppComponent
import com.missapps.brabet.di.DaggerAppComponent

class App : Application() {
    val component: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
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
    }
}
