package com.bbbrbetss.comebbrabettt.app.levels

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bbbrbetss.comebbrabettt.app.core.coroutines.DispatchersProvider
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

@Singleton
class GameInfoRepository @Inject constructor(
    private val appContext: Context,
    private val dispatchersProvider: DispatchersProvider
) {
    private val Context.dataStore by preferencesDataStore(name = "unlocked levels")
    private val unlockedLevelsCount = intPreferencesKey("unlocked_levels_count")

    private var levelsInfo: List<LevelInfo>? = null

    fun getUnlockedLevelsCountFlow(): Flow<Int> = appContext.dataStore
        .data
        .map { it[unlockedLevelsCount] ?: 1 }
        .distinctUntilChanged()
        .flowOn(dispatchersProvider.io)

    suspend fun getUnlockedLevelsCount(): Int = getUnlockedLevelsCountFlow().first()

    suspend fun setUnlockedLevelsCount(count: Int) = withContext(dispatchersProvider.io) {
        appContext.dataStore.edit {
            it[unlockedLevelsCount] = count
        }
    }

    suspend fun getLevelsInfo(): List<LevelInfo> = withContext(dispatchersProvider.io) {
        levelsInfo ?: appContext.assets.open("levels-info.json")
            .bufferedReader()
            .use { reader ->
                Json.decodeFromString<List<LevelInfo>>(reader.readText()).also { levelsInfo = it }
            }
    }
}
