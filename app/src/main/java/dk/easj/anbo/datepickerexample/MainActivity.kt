package dk.easj.anbo.datepickerexample

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import dk.easj.anbo.datepickerexample.databinding.ActivityMainBinding
import java.text.DateFormat

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
                "Unit time in miliseconds: ${meetingStart.timeInMillis}"

            val timeInSeconds = convertCalendarToTimeInSeconds(meetingStart)
            binding.unixTimeSecondsTextView.text = "Unix time in seconds: $timeInSeconds"
        }
    }

    private fun convertCalendarToTimeInSeconds(calendar: Calendar): Int {
        val timeInMillis = calendar.timeInMillis
        return (timeInMillis / 1000).toInt()
    }
}