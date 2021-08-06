package com.better.learn.jetpack.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.better.learn.jetpack.SettingsPb
import java.io.InputStream
import java.io.OutputStream

// 使用 Preferences DataStore 存储键值对
// 在Context中申明了一个变量
val Context.dataStorePreference: DataStore<Preferences> by preferencesDataStore(name = "settings")

// 使用Proto
object Settings2Serializer : Serializer<SettingsPb> {

    override val defaultValue: SettingsPb
        get() = SettingsPb.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SettingsPb {
        return try {
            //Inappropriate blocking method call
            //https://www.reddit.com/r/Kotlin/comments/mr3j45/best_way_to_call_inappropriate_blocking_code_with/
            SettingsPb.parseFrom(input)
        } catch (exception: Exception) {
            exception.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: SettingsPb, output: OutputStream) {
        t.writeTo(output)
    }
}

val Context.dataStoreProtobuf: DataStore<SettingsPb> by dataStore(
    fileName = "settingspb.pb",
    serializer = Settings2Serializer
)