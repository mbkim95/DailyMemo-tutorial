package com.project.dailymemo.data

import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import java.util.*

class MemoDao(private val realm: Realm) {

    fun getAllMemos(): RealmResults<MemoData> {
        return realm.where(MemoData::class.java)
            .sort("createdAt", Sort.DESCENDING)
            .findAll()
    }

    fun selectMemo(id: String): MemoData {
        return realm.where(MemoData::class.java)
            .equalTo("id", id)
            .findFirst() as MemoData
    }

    fun addOrUpdateMemo(memoData: MemoData, title: String, content: String) {
        realm.executeTransaction {
            memoData.title = title
            memoData.content = content
            memoData.createdAt = Date()

            if (content.length > 100) {
                memoData.summary = content.substring(0..100)
            } else {
                memoData.summary = content
            }

            if (!memoData.isManaged) {
                it.copyToRealm(memoData)
            }
        }
    }
}