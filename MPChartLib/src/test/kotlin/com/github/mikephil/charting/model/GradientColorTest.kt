package com.github.mikephil.charting.model

import android.graphics.Color
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class GradientColorTest {
    @Suppress("ReplaceCallWithBinaryOperator")
    @Test
    fun equals() {
        val gradientColor = GradientColor(Color.RED, Color.GREEN)

        assertThat(gradientColor.startColor).isEqualTo(Color.RED)
        assertThat(gradientColor.endColor).isEqualTo(Color.GREEN)
        assertThat(gradientColor.equals(null)).isFalse()
        assertThat(gradientColor.equals(42)).isFalse()
        assertThat(gradientColor.equals("")).isFalse()
        assertThat(gradientColor.equals(GradientColor(Color.GREEN, Color.RED))).isFalse()
        assertThat(gradientColor.equals(GradientColor(Color.RED, Color.GREEN))).isTrue()
        assertThat(gradientColor.equals(GradientColor(Color.RED, Color.YELLOW))).isFalse()
        assertThat(gradientColor.equals(GradientColor(Color.BLUE, Color.YELLOW))).isFalse()

        gradientColor.startColor = Color.GREEN
        gradientColor.endColor = Color.RED

        assertThat(gradientColor.startColor).isEqualTo(Color.GREEN)
        assertThat(gradientColor.endColor).isEqualTo(Color.RED)
        assertThat(gradientColor.equals(null)).isFalse()
        assertThat(gradientColor.equals(42)).isFalse()
        assertThat(gradientColor.equals("")).isFalse()
        assertThat(gradientColor.equals(GradientColor(Color.RED, Color.GREEN))).isFalse()
        assertThat(gradientColor.equals(GradientColor(Color.GREEN, Color.RED))).isTrue()
        assertThat(gradientColor.equals(GradientColor(Color.GREEN, Color.YELLOW))).isFalse()
        assertThat(gradientColor.equals(GradientColor(Color.BLUE, Color.YELLOW))).isFalse()
    }
}
