package com.example.geoquiz

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

data class Question(val textResId: Int, val answer: Boolean)

class MainActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    private val questionBank = listOf(
        Question(R.string.question_1, true),
        Question(R.string.question_2, true),
        Question(R.string.question_3, false),
        Question(R.string.question_4, false),
        Question(R.string.question_5, true),
        Question(R.string.question_6, true)
    )

    private var currentIndex = 0
    private var score = 0
    private var answered = BooleanArray(questionBank.size)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Restore state after rotation
        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_INDEX, 0)
            score = savedInstanceState.getInt(KEY_SCORE, 0)
            answered = savedInstanceState.getBooleanArray(KEY_ANSWERED) ?: answered
        }

        questionTextView = findViewById(R.id.question_text_view)
        scoreTextView = findViewById(R.id.score_text_view)
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)

        trueButton.setOnClickListener { checkAnswer(true) }
        falseButton.setOnClickListener { checkAnswer(false) }

        findViewById<Button>(R.id.next_button).setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }
        findViewById<Button>(R.id.prev_button).setOnClickListener {
            currentIndex = (currentIndex - 1 + questionBank.size) % questionBank.size
            updateQuestion()
        }
        findViewById<Button>(R.id.cheat_button).setOnClickListener {
            // Explicit Intent to the second Activity, with the answer as an extra
            val intent = CheatActivity.newIntent(this, questionBank[currentIndex].answer)
            startActivity(intent)
        }

        updateQuestion()
    }

    private fun updateQuestion() {
        questionTextView.setText(questionBank[currentIndex].textResId)
        // Only allow answering each question once
        val enabled = !answered[currentIndex]
        trueButton.isEnabled = enabled
        falseButton.isEnabled = enabled
        showScoreIfFinished()
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correct = questionBank[currentIndex].answer == userAnswer
        if (correct) score++
        answered[currentIndex] = true
        trueButton.isEnabled = false
        falseButton.isEnabled = false

        val message = if (correct) R.string.correct_toast else R.string.incorrect_toast
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        showScoreIfFinished()
    }

    private fun showScoreIfFinished() {
        scoreTextView.text = if (answered.all { it }) {
            "Final score: $score / ${questionBank.size}"
        } else {
            ""
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_INDEX, currentIndex)
        outState.putInt(KEY_SCORE, score)
        outState.putBooleanArray(KEY_ANSWERED, answered)
    }

    companion object {
        private const val KEY_INDEX = "index"
        private const val KEY_SCORE = "score"
        private const val KEY_ANSWERED = "answered"
    }
}