package com.example.examplenumberpicker

import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<LinearLayoutCompat>(R.id.layout).apply {
            addView(createNumberPicker())
        }
    }

    // Simple NumberPicker instance
    private fun createNumberPicker(): NumberPicker {
        return NumberPicker(applicationContext).apply {
            maxValue = 3
            minValue = 0
            displayedValues = arrayOf("00", "15", "30", "45")

            setAccessibility()
        }
    }

    // Accessibility setup
    private fun NumberPicker.setAccessibility() {

        // 1. contentDescription - not working
        for (i in 0 until childCount) {
            getChildAt(i).apply {
                contentDescription = "Content description #1"
            }
        }

        // 2. accessibility delegate - not working
        for (i in 0 until childCount) {
            getChildAt(i).apply {

                ViewCompat.setAccessibilityDelegate(this, object :
                    AccessibilityDelegateCompat() {
                    override fun onInitializeAccessibilityNodeInfo(
                        host: View,
                        info: AccessibilityNodeInfoCompat
                    ) {
                        super.onInitializeAccessibilityNodeInfo(host, info)
                        info.contentDescription = "Content description #2"
                    }
                })
            }
        }

        // 3. accessibility node provider - works partially - only for picked number, not for the rest
        for (i in 0 until childCount) { // child count = 1
            getChildAt(i).apply {

                ViewCompat.setAccessibilityDelegate(this, object :
                    AccessibilityDelegateCompat() {
                    override fun getAccessibilityNodeProvider(host: View):
                            AccessibilityNodeProviderCompat {
                        return object : AccessibilityNodeProviderCompat() {
                            override fun
                                    createAccessibilityNodeInfo(virtualViewId: Int):
                                    AccessibilityNodeInfoCompat? {
                                return AccessibilityNodeInfoCompat.obtain().apply {
                                    contentDescription = "Content description #3"
                                }
                            }
                        }
                    }
                })
            }
        }
}