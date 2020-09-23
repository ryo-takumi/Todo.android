package com.example.todo

import io.realm.RealmObject

open class TaskDB : RealmObject() {
    open var strTime: String = ""
    open var strTask: String = ""
    open var finishFrag: Boolean = false

}