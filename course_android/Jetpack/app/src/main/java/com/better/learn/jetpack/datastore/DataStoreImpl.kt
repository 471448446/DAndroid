package com.better.learn.jetpack.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// 使用 Preferences DataStore 存储键值对
// 在Context中申明了一个变量
val Context.dataStorePreference: DataStore<Preferences> by preferencesDataStore(name = "settings")