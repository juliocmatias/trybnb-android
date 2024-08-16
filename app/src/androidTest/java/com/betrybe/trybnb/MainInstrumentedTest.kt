package com.betrybe.trybnb

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry

private fun getId(id: String): Int {
    val targetContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
    val packageName: String = targetContext.packageName
    return targetContext.resources.getIdentifier(id, "id", packageName)
}

