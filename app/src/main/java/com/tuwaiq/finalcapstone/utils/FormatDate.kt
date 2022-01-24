package com.tuwaiq.finalcapstone.utils

import java.text.SimpleDateFormat
import java.util.*

class FormatDate {

    operator fun invoke(date: Date, spf2: String): String {
        var spf = SimpleDateFormat("E LLL dd hh:mm:ss z yyyy")
        val parsed = spf.parse(date.toString())
        spf = SimpleDateFormat(spf2)
        return spf.format(parsed)
    }

}