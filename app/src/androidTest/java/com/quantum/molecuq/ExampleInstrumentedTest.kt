package com.quantum.molecuq

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device or emulator.
 * For more info: http://d.android.com/tools/testing
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    fun testAppContextPackageName() {
        // Get the target app context
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        // Verify the package name
        assertEquals("com.quantum.molecuq", appContext.packageName)
    }
}
