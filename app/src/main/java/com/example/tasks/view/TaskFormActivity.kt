package com.example.tasks.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.service.model.TaskModel
import com.example.tasks.viewmodel.RegisterViewModel
import com.example.tasks.viewmodel.TaskFormViewModel
import kotlinx.android.synthetic.main.activity_task_form.*
import java.text.SimpleDateFormat
import java.util.*

class TaskFormActivity : AppCompatActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private lateinit var mViewModel: TaskFormViewModel
    private val mDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    private val mListPriorityId: MutableList<Int> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        mViewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)

        // Inicializa eventos
        listeners()
        observe()

        mViewModel.listPriorities()
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.button_save) {
            handleSave()
        } else if (id == R.id.button_date){
            showDatePicker()

        }
    }

    private fun handleSave(){
        val task = TaskModel().apply {
            this.description = edit_description.text.toString()
            this.complete = check_complete.isChecked
            this.dueDate = button_date.text.toString()
            this.priorityId = mListPriorityId[spinner_priority.selectedItemPosition].toString()

        }

        mViewModel.save(task)

    }

    private fun observe() {
        mViewModel.priorities.observe(this, androidx.lifecycle.Observer {

            val list: MutableList<String> = arrayListOf()
            for(item in it){
                list.add(item.description)
                mListPriorityId.add(item.id)
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
            spinner_priority.adapter = adapter
        })
    }

    private fun showDatePicker(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        DatePickerDialog(this, this, year, month, day).show()
    }

    private fun listeners() {
        button_save.setOnClickListener(this)
        button_date.setOnClickListener(this)
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        val srt = mDateFormat.format(calendar.time)
        button_date.text = srt
    }

}
