package com.saihu.common.util

import android.text.TextUtils
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import java.math.BigDecimal
import java.math.RoundingMode

fun String.toBigNumber(): BigDecimal {
    return this.toBigDecimal().stripTrailingZeros()
}

operator fun Any.plus(value: Any): BigDecimal {
    val num1 = this.toString().toBigNumber()
    val num2 = value.toString().toBigNumber()
    return num1.add(num2)
}

operator fun Any.minus(value: Any): BigDecimal {
    val num1 = this.toString().toBigNumber()
    val num2 = value.toString().toBigNumber()
    return num1.subtract(num2)
}

operator fun Any.times(value: Any): BigDecimal {
    val num1 = this.toString().toBigNumber()
    val num2 = value.toString().toBigNumber()
    return num1.multiply(num2)
}

operator fun Any.div(value: Any): BigDecimal {
    val num1 = this.toString().toBigNumber()
    val num2 = value.toString().toBigNumber()
    return num1.divide(num2, 2, RoundingMode.HALF_UP)
}

operator fun Any.compareTo(value: Any): Int {
    val num1 = this.toString().toBigNumber()
    val num2 = value.toString().toBigNumber()
    return num1.compareTo(num2)
}

private val MOBILE_REGEX =
    Regex(pattern = "^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))(\\d{8})$")

fun String.isMobile() = if (TextUtils.isEmpty(this)) false else matches(MOBILE_REGEX)

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }


@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }