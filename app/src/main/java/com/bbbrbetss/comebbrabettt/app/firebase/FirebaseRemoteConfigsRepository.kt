package com.bbbrbetss.comebbrabettt.app.firebase

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ktx.configUpdates
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.bbbrbetss.comebbrabettt.app.core.coroutines.DispatchersProvider
import dagger.Reusable
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Reusable
class FirebaseRemoteConfigsRepository @Inject constructor(
    private val dispatchersProvider: DispatchersProvider
) {
    val showWebview: Boolean get() = Firebase.remoteConfig.getBoolean("showWebview")
    val webviewLink: String get() = Firebase.remoteConfig.getString("webviewLink")

    suspend fun startInitialFetch(): Unit = withContext(dispatchersProvider.io) {
        Firebase.remoteConfig.fetchAndActivate().await()
        merge<ConfigUpdate?>(
            flow {
                delay(TIME_FOR_WAITING_UPDATES)
                emit(null)
            },
            Firebase.remoteConfig.configUpdates
        ).first()?.let { Firebase.remoteConfig.activate().await() }
    }

    private companion object {
        const val TIME_FOR_WAITING_UPDATES = 4000L
    }
}
