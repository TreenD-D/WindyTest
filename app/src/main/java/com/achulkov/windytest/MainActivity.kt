package com.achulkov.windytest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //no viewbinding just cause it is not in the scope of task, using old plain findbyid
        val editText = findViewById<EditText>(R.id.editTextNumber)
        val button = findViewById<Button>(R.id.button)
        val textView = findViewById<TextView>(R.id.textView)

        button.setOnClickListener {
            textView.text = ""
            val count = editText.text.toString().toInt()
            viewModel.startFlow(count)
        }

        //would be using viewlifecycleowner for fragment
        this@MainActivity.lifecycleScope.launch {
            this@MainActivity.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resultFlow.collect { value ->
                    textView.append("$value\n")
                }
            }
        }
    }
}