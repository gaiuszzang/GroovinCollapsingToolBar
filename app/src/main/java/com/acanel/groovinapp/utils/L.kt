package com.acanel.groovinapp.utils

import android.util.Log

object L {
    private const val TAG = "Groovin"

    fun d(tag: String?, msg: String) = Log.d(TAG, "[$tag] $msg")
    fun i(tag: String?, msg: String) = Log.i(TAG, "[$tag] $msg")
    fun v(tag: String?, msg: String) = Log.v(TAG, "[$tag] $msg")
    fun w(tag: String?, msg: String) = Log.w(TAG, "[$tag] $msg")
    fun e(tag: String?, msg: String) = Log.e(TAG, "[$tag] $msg")
}