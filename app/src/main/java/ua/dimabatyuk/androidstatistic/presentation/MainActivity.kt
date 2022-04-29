package ua.dimabatyuk.androidstatistic.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ua.dimabatyuk.androidstatistic.R
import java.lang.Integer.min

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
                        .let {

                            val step = 3
                            val sp = SpannableString(it)
                            for (i: Int in 0..it.length step step) {
                                val color = if (i % 2 == 0) {
                                    ForegroundColorSpan(getColor(R.color.black))
                                } else {
                                    ForegroundColorSpan(getColor(R.color.dark_grey))
                                }

                                sp.setSpan(
                                    color,
                                    i,
                                    min(i+step, it.length),
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                    )
                            }

                            sp
                        }
                }
        }
    }
}