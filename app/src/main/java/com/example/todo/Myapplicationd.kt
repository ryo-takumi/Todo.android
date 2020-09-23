package com.example.todo

import android.app.Application
import io.realm.Realm

class Myapplicationd : Application() {

    override fun onCreate() {
        super.onCreate()
                Realm.init(this)
    }
}
