package ua.dimabatyuk.androidstatistic.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ua.dimabatyuk.androidstatistic.R

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val androidIdValue = findViewById<TextView>(R.id.android_id_value)

        lifecycleScope.launch {
            viewModel.state
                .collect {
                    androidIdValue.text = it.androidId.toString()
                }
        }
    }
}