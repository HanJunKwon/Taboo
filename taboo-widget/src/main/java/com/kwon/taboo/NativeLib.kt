package com.kwon.taboo

class NativeLib {

    /**
     * A native method that is implemented by the 'taboo' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'taboo' library on application startup.
        init {
            System.loadLibrary("taboo")
        }
    }
}