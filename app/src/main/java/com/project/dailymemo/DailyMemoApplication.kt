package com.project.dailymemo

import android.app.Application
import io.realm.Realm

class DailyMemoApplication() : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}