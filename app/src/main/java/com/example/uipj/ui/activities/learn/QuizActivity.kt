package com.example.uipj.ui.activities.learn

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.uipj.R
import com.example.uipj.data.dao.CardDAO
import com.example.uipj.data.dao.FlashCardDAO
import com.example.uipj.data.model.Card
import com.example.uipj.databinding.ActivityQuizBinding
import com.example.uipj.databinding.DialogCorrectBinding
import com.example.uipj.databinding.DialogWrongBinding
import com.saadahmedev.popupdialog.PopupDialog
import kotlinx.coroutines.*

class QuizActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityQuizBinding.inflate(layoutInflater)
    }
    private val cardDAO by lazy {
        CardDAO(this)
    }
    private val flashCardDAO by lazy {
        FlashCardDAO(this)
    }

    private var progress = 0
    private lateinit var correctAnswer: String
    private val askedCards = mutableListOf<Card>()
    private lateinit var id: String
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        id = intent.getStringExtra("id") ?: ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        setNextQuestion()
        val max = cardDAO.getCardByIsLearned(id, 0).size
        binding.timelineProgress.max = max
    }

    private fun checkAnswer(selectedAnswer: String, cardId: String): Boolean {
        return if (selectedAnswer == correctAnswer) {
            correctDialog(correctAnswer)
            GlobalScope.launch(Dispatchers.IO) {
                cardDAO.updateIsLearnedCardById(cardId, 1)
            }
            setNextQuestion()
            progress++
            setUpProgressBar()
            true
        } else {
            wrongDialog(correctAnswer, binding.tvQuestion.text.toString(), selectedAnswer)
            setNextQuestion()
            false
        }
    }

    private fun setUpProgressBar() {
        binding.timelineProgress.progress = progress
        Log.d("progresss", progress.toString())
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun setNextQuestion() {
        scope.launch {
            val cards = cardDAO.getCardByIsLearned(id, 0) // get a list of cards that are not learned
            val randomCard = cardDAO.getAllCardByFlashCardId(id) // get all cards

            if (cards.isEmpty()) {
                finishQuiz()
                return@launch

            }

            val correctCard = cards.random() // get a random card from a list of cards that are not learned
            randomCard.remove(correctCard) // remove the correct card from a list of all cards

            val incorrectCards = randomCard.shuffled().take(3) // get 3 random cards from list of all cards

            val allCards = (listOf(correctCard) + incorrectCards).shuffled() // shuffle 4 cards
            val question = correctCard.front
            correctAnswer = correctCard.back

            withContext(Dispatchers.Main) {
                binding.tvQuestion.text = question
                binding.optionOne.text = allCards[0].back
                binding.optionTwo.text = allCards[1].back
                binding.optionThree.text = allCards[2].back
                binding.optionFour.text = allCards[3].back

                binding.optionOne.setOnClickListener {
                    checkAnswer(binding.optionOne.text.toString(), correctCard.id)
                }

                binding.optionTwo.setOnClickListener {
                    checkAnswer(binding.optionTwo.text.toString(), correctCard.id)
                }

                binding.optionThree.setOnClickListener {
                    checkAnswer(binding.optionThree.text.toString(), correctCard.id)
                }

                binding.optionFour.setOnClickListener {
                    checkAnswer(binding.optionFour.text.toString(), correctCard.id)
                }

                askedCards.add(correctCard)


            }
        }
    }

    private fun finishQuiz() { //1 quiz, 2 learn
        runOnUiThread {
            PopupDialog.getInstance(this)
                .statusDialogBuilder()
                .createSuccessDialog()
                .setHeading(getString(R.string.finish))
                .setDescription(getString(R.string.finish_quiz))
                .build(Dialog::dismiss)
                .show();
        }

    }

    private fun correctDialog(answer: String) {
        val dialog = AlertDialog.Builder(this)
        val dialogBinding = DialogCorrectBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        val builder = dialog.create()
        dialogBinding.questionTv.text = answer
        dialog.setOnDismissListener {
            // startAnimations()
        }


        builder.show()

    }

    private fun wrongDialog(answer: String, question: String, userAnswer: String) {
        val dialog = AlertDialog.Builder(this)
        val dialogBinding = DialogWrongBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        val builder = dialog.create()
        dialogBinding.questionTv.text = question
        dialogBinding.explanationTv.text = answer
        dialogBinding.yourExplanationTv.text = userAnswer
        dialogBinding.continueTv.setOnClickListener {
            builder.dismiss()
        }
        builder.setOnDismissListener {
            //startAnimations()
        }
        builder.show()
    }

    private fun startAnimations() {
        val views =
            listOf(
                binding.optionOne,
                binding.optionTwo,
                binding.optionThree,
                binding.optionFour,
                binding.tvQuestion
            )
        val duration = 1000L
        val endValue = -binding.optionOne.width.toFloat()

        views.forEach { view ->
            val animator = ObjectAnimator.ofFloat(view, "translationX", 0f, endValue)
            animator.duration = duration
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.translationX = 0f
                    if (view == binding.optionFour) {
                        setNextQuestion()
                    }
                }
            })
            animator.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}