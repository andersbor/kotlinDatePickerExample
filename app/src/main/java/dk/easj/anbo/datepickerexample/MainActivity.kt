package dk.easj.anbo.datepickerexample

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.icu.util.Calendar
import android.icu.util.TimeZone
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.timepicker.TimeFormat
import dk.easj.anbo.datepickerexample.databinding.ActivityMainBinding
import java.text.DateFormat
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val meetingStart = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        val currentMoment: Long = Clock.system(ZoneId.of("Europe/Copenhagen")).millis()
        val t: Long = System.currentTimeMillis()
        Log.d("APPLE", "currentMoment unit time $currentMoment")
        Log.d("APPLE", "currentMoment unit time $t")

        val dateFormatter = DateFormat.getDateInstance(DateFormat.LONG, Locale.FRANCE)
        val timeFormatter = DateFormat.getTimeInstance() // using device locale
        val dateString = dateFormatter.format(currentMoment)
        val timeString = timeFormatter.format(currentMoment)
        Log.d("APPLE", "currentMoment $dateString $timeString")


        binding.dateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val currentYear = calendar[Calendar.YEAR]
            val currentMonth = calendar[Calendar.MONTH]
            val currentDayOfMonth = calendar[Calendar.DATE]
            val dateSetListener = OnDateSetListener { datePicker, year, month, dayOfMonth ->
                meetingStart.set(Calendar.YEAR, year)
                meetingStart.set(Calendar.MONTH, month)
                meetingStart.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val dateFormatter = DateFormat.getDateInstance()
                val dateString = dateFormatter.format(meetingStart.timeInMillis)
                binding.dateButton.text = dateString
            }
            val dialog = DatePickerDialog(
                this, dateSetListener, currentYear, currentMonth, currentDayOfMonth
            )
            dialog.show()
        }

        binding.timeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val currentHourOfDay = calendar[Calendar.HOUR_OF_DAY]
            val currentMinute = calendar[Calendar.MINUTE]
            val timeSetListener = OnTimeSetListener { timePicker, hourOfDay, minute ->
                meetingStart[Calendar.HOUR_OF_DAY] = hourOfDay
                meetingStart[Calendar.MINUTE] = minute
                val timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT)
                val timeString = timeFormatter.format(meetingStart.timeInMillis)
                binding.timeButton.text = timeString
            }
            val dialog = TimePickerDialog(
                this, timeSetListener, currentHourOfDay, currentMinute, true
            )
            dialog.show()
        }

        binding.showTimesButton.setOnClickListener {
            val dateFormatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.LONG)
            val date = meetingStart.time
            val dateTimeString = dateFormatter.format(date)
            binding.timeTextView.text = dateTimeString

            binding.unixTimeMiliSecondsTextView.text =
                "Unit time in milliseconds: ${meetingStart.timeInMillis}"

            val timeInSeconds = convertCalendarToTimeInSeconds(meetingStart)
            binding.unixTimeSecondsTextView.text = "Unix time in seconds: $timeInSeconds"
        }
    }

    private fun convertCalendarToTimeInSeconds(calendar: Calendar): Int {
        val timeInMillis: Long = calendar.timeInMillis
        return (timeInMillis / 1000).toInt()
    }
}