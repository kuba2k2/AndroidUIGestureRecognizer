package it.sephiroth.android.library.uigestures.demo

import android.util.Log
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import it.sephiroth.android.library.uigestures.UIGestureRecognizer.State
import it.sephiroth.android.library.uigestures.UITapGestureRecognizer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class TestTapGesture : TestBaseClass() {

    @Test
    fun test_singleTap() {
        val latch = CountDownLatch(1)
        val activity = activityTestRule.activity
        val delegate = activity.delegate
        assertNotNull(delegate)
        delegate.clear()

        val recognizer = UITapGestureRecognizer(context)
        recognizer.tag = "single-tap"
        recognizer.touchesRequired = 1
        recognizer.tapsRequired = 1
        recognizer.actionListener = {
            assertEquals(State.Ended, it.state)
            latch.countDown()
        }
        delegate.addGestureRecognizer(recognizer)

        textView.text = "None"

        onView(ViewMatchers.withId(R.id.activity_main)).perform(ViewActions.click())
        latch.await()
    }

    @Test
    fun test_singleTap2Fingers() {
        val latch = CountDownLatch(1)
        val delegate = activityTestRule.activity.delegate
        assertNotNull(delegate)
        delegate.clear()

        val recognizer = UITapGestureRecognizer(context)
        recognizer.tag = "single-tap"
        recognizer.touchesRequired = 2
        recognizer.tapsRequired = 1

        recognizer.actionListener = {
            assertEquals(State.Ended, it.state)
            latch.countDown()
        }

        delegate.addGestureRecognizer(recognizer)

        val pt1 = super.randomPointOnScreen()
        val pt2 = super.randomPointOnScreen()

        Log.v(TAG, "pt1: $pt1")
        Log.v(TAG, "pt2: $pt2")

        mainView.performTwoPointerGesture(pt1, pt2, pt1, pt2, 2)
        latch.await()
    }

    @Test
    fun test_doubleTap() {
        val latch = CountDownLatch(1)
        val activity = activityTestRule.activity

        val delegate = activity.delegate
        delegate.clear()

        val recognizer = UITapGestureRecognizer(context)
        recognizer.tag = "double-tap"
        recognizer.touchesRequired = 1
        recognizer.tapsRequired = 2
        recognizer.tapTimeout = 400
        recognizer.actionListener = {
            assertEquals(State.Ended, it.state)
            latch.countDown()
        }

        delegate.addGestureRecognizer(recognizer)

        textView.text = "None"

        onView(ViewMatchers.withId(R.id.activity_main)).perform(ViewActions.doubleClick())
        latch.await()
    }

}
