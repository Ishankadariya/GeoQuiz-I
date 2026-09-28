package com.example.geoquiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private var answerIsTrue = false
    private var answerShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        answerShown = savedInstanceState?.getBoolean(KEY_SHOWN, false) ?: false

        answerTextView = findViewById(R.id.answer_text_view)
        findViewById<Button>(R.id.show_answer_button).setOnClickListener {
            answerShown = true
            displayAnswer()
        }

        if (answerShown) displayAnswer()
    }

    private fun displayAnswer() {
        answerTextView.setText(if (answerIsTrue) R.string.answer_true else R.string.answer_false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_SHOWN, answerShown)
    }

    companion object {
        private const val EXTRA_ANSWER_IS_TRUE = "com.example.geoquiz.answer_is_true"
        private const val KEY_SHOWN = "answer_shown"

        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}