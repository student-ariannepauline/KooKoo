package com.sunnyside.kookoo.student.ui.pickers

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.sunnyside.kookoo.student.model.JoinedClassModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class DatePicker(private val listener: (LocalDate) -> Unit) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        val selectedDate = LocalDate.parse("${p1}-${(p2 + 1).toString().padStart(2, '0')}-${p3.toString().padStart(2, '0')}")
        listener(selectedDate)
    }
}