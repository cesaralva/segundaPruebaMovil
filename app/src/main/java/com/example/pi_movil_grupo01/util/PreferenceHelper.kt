package com.example.pi_movil_grupo01.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object PreferenceHelper {


    // SharedPreferences predeterminadas
    fun defaultPrefs(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    // SharedPreferences personalizadas
    fun customPrefs(context: Context, name: String): SharedPreferences {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    // Método para guardar un valor en SharedPreferences
    fun put(sharedPreferences: SharedPreferences, key: String, value: Any) {
        with(sharedPreferences.edit()) {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                else -> throw UnsupportedOperationException("Tipo no implementado")
            }
            apply()
        }
    }

    // Método para obtener un valor de SharedPreferences
    @Suppress("UNCHECKED_CAST")
    fun <T> get(sharedPreferences: SharedPreferences, key: String, defaultValue: T): T {
        val result: Any = when (defaultValue) {
            is String -> sharedPreferences.getString(key, defaultValue) ?: defaultValue
            is Int -> sharedPreferences.getInt(key, defaultValue)
            is Boolean -> sharedPreferences.getBoolean(key, defaultValue)
            is Float -> sharedPreferences.getFloat(key, defaultValue)
            is Long -> sharedPreferences.getLong(key, defaultValue)
            else -> throw UnsupportedOperationException("Tipo no implementado")
        }
        return result as T
    }
}