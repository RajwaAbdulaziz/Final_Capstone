package com.tuwaiq.finalcapstone.repo

import android.content.Context

import java.io.File

class Repo private constructor(context: Context) {

    private val fileDir = context.applicationContext.filesDir



    companion object{
        private var INSTANCE:Repo? = null

        fun initialize(context: Context){
            if (INSTANCE == null){
                INSTANCE = Repo(context)
            }
        }

        fun getInstance():Repo = INSTANCE ?: throw IllegalStateException("Initialize your repo first")
    }
}