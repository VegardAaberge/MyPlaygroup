package com.myplaygroup.app.core.data.settings

import androidx.datastore.core.Serializer
import com.google.crypto.tink.Aead
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
class UserSettingsSerializer(private val aead: Aead) : Serializer<UserSettings> {
    override val defaultValue: UserSettings
        get() = UserSettings()

    override suspend fun readFrom(input: InputStream): UserSettings {
        return try {
            val encryptedInput = input.readBytes()
            if(encryptedInput.isEmpty()){
                defaultValue
            }

            val decryptedInput = aead.decrypt(encryptedInput, null)
            Json.decodeFromString(
                deserializer = UserSettings.serializer(),
                string = decryptedInput.decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: UserSettings, output: OutputStream) {
        val byteArray = Json.encodeToString(
            serializer = UserSettings.serializer(),
            value = t
        ).encodeToByteArray()
        val encryptedBytes = aead.encrypt(byteArray, null)

        output.write(encryptedBytes)
    }
}