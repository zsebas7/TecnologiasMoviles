package com.undef.superahorro.caparrozruiz.util

import java.util.Locale

fun Double.toCurrencyString(locale: Locale): String = "%.2f".format(locale, this)
