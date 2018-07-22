package com.example.demo.openglapplication.etc

interface StateChangeListener {

    fun onStateChanged(lastState:Int,nowState:Int)

    companion object {
        const val START=1
        const val STOP=2
        const val PLAYING=3
        const val INIT=4
        const val PAUSE=5
        const val RESUME=6

    }
}
