package com.sunnyside.kookoo.student.ui.pickers

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat;
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.time.LocalTime
import java.util.*

class TimePicker(private val listener: (LocalTime) -> Unit) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        val selectedTime = LocalTime.of(p1, p2)
        listener(selectedTime)
    }

}