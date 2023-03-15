package cz.mendelu.xmusil5.plantmonitor.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import cz.mendelu.xmusil5.plantmonitor.datastore.DataStoreConstants

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = DataStoreConstants.SETTINGS_DATA_STORE)