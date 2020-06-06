package com.example.android.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.navigation.databinding.FragmentGameBinding

class GameFragment : Fragment() {
    data class Question(
            val text: String,
            val answers: List<String>)

    // The first answer is the correct one.  We randomize the answers before showing the text.
    // All questions must have four answers.  We'd want these to contain references to string
    // resources so we could internationalize. (or better yet, not define the questions in code...)
    private val questions: MutableList<Question> = mutableListOf(
            Question(text = "What country has the longest coastline?",
                    answers = listOf("Canada", "India", "U.S.A", "China")),
            Question(text = "What is the capital of New Zealand?",
                    answers = listOf("Wellington", "Auckland", "None of these", "Canterbury")),
            Question(text = "Approximately what is the age of the earth?",
                    answers = listOf("4.5 billion years", "3.5 billion years", " 4 billion years", "2.5 billion years")),
            Question(text = "Big Bang theory explains ?",
                    answers = listOf("Origin of Universe.", "Origin of Sun.", "Laws of physics.", "None of above.")),
            Question(text = "The great Victoria Desert is located in?",
                    answers = listOf("Australia", "Africa", "Asia", "Europe")),
            Question(text = "The light of distant stars is affected by?",
                    answers = listOf("both of them", "None", "the earth's atmosphere", "interstellar dust")),
            Question(text = "Which of the following is tropical grassland?",
                    answers = listOf("Savannah", "Taiga", "Pampas", "Prairies")),
            Question(text = "The temperature increases rapidly after?",
                    answers = listOf("ionosphere", "stratosphere", "troposphere", "exosphere")),
            Question(text = "The largest glaciers are?",
                    answers = listOf("continental glaciers", "piedmont glaciers", "mountain glaciers", "alpine glaciers")),
            Question(text = "What is the capital of South Korea?",
                    answers = listOf("Seoul", "Busan", "Gwangju", "Sacheon")),
            Question(text = "The leading state in producing paper is?",
                    answers = listOf("West Bengal", "Madhya Pradesh", "Kerala", "Uttar Pradesh")),
            Question(text = "The luminous coloured ring, surrounding the sun is called the?",
                    answers = listOf("Corona", "Nebula", "Comet", "Solar Storm")),
            Question(text = "The most salty sea in the world is?",
                    answers = listOf("Dead Sea", "Red Sea", "Arabian Sea", "Mediterranean Sea")),
            Question(text = "Capital of Germany?",
                    answers = listOf("Berlin", "Munich", "Frankfurt", "Hamburg")),
            Question(text = "World's most Cutest Person?",
                    answers = listOf("Muskan Jain", "Dipit Vasdev", "None", "Kunal Pal"))
    )

    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private val numQuestions = Math.min((questions.size + 1) / 2, 3)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentGameBinding>(
                inflater, R.layout.fragment_game, container, false)

        // Shuffles the questions and sets the question index to the first question.
        randomizeQuestions()

        // Bind this fragment class to the layout
        binding.game = this

        // Set the onClickListener for the submitButton
        binding.submitButton.setOnClickListener @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        { view: View ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            // Do nothing if nothing is checked (id == -1)
            if (-1 != checkedId) {
                var answerIndex = 0
                when (checkedId) {
                    R.id.secondAnswerRadioButton -> answerIndex = 1
                    R.id.thirdAnswerRadioButton -> answerIndex = 2
                    R.id.fourthAnswerRadioButton -> answerIndex = 3
                }
                // The first answer in the original question is always the correct one, so if our
                // answer matches, we have the correct answer.
                if (answers[answerIndex] == currentQuestion.answers[0]) {
                    questionIndex++
                    // Advance to the next question
                    if (questionIndex < numQuestions) {
                        currentQuestion = questions[questionIndex]
                        setQuestion()
                        binding.invalidateAll()
                    } else {
                        // We've won!  Navigate to the gameWonFragment.
                        view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameWonFragment(numQuestions,questionIndex))
                    }
                } else {
                    // Game over! A wrong answer sends us to the gameOverFragment.
                    view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameOverFragment2())
                }
            }
        }
        return binding.root
    }

    // randomize the questions and set the first question
    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }

    // Sets the question and randomizes the answers.  This only changes the data, not the UI.
    // Calling invalidateAll on the FragmentGameBinding updates the data.
    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        // randomize the answers into a copy of the array
        answers = currentQuestion.answers.toMutableList()
        // and shuffle them
        answers.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_android_trivia_question, questionIndex + 1, numQuestions)
    }
}
