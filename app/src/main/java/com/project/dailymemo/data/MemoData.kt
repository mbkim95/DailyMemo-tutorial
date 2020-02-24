package com.project.dailymemo.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class MemoData(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var createdAt: Date = Date(),
    var title: String = "",
    var content: String = "",
    var summary: String = "",
    var imageFile: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var alarmTime: Date = Date(),
    var weather: String = ""
): RealmObject()