package com.ziyad.wordup


import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ziyad.wordup.databinding.FragmentWordMeaningDialogBinding
import com.ziyad.wordup.recyclerview.WordModel
import org.json.JSONObject
import java.io.InputStreamReader

class WordMeaningDialogFragment : DialogFragment() {

    private var _binding: FragmentWordMeaningDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var word: WordModel
    private lateinit var wordList : List<WordModel>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWordMeaningDialogBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val wordId = WordMeaningDialogFragmentArgs.fromBundle(requireArguments()).wordId
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", 0)
        wordList = loadWordList()
        word = findWord(wordId)

        binding.wordTextView.text = word.word
        binding.pronunciationTextView.text = word.pronunciation
        binding.turkishTextView.text = word.turkish
        binding.azerbaijaniTextView.text = word.azerbaijani

        binding.btnKnown.setOnClickListener {
            saveIdToPreferences(word.id)
            dismiss()
            Toast.makeText(requireContext(), "${word.word} added to known words", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadWordList(): List<WordModel> {
        val inputStream = context?.resources?.openRawResource(R.raw.words)
        val jsonString = InputStreamReader(inputStream).use { it.readText() }
        val jsonObject = JSONObject(jsonString)
        val jsonArray = jsonObject.getJSONArray("words")
        val type = object : TypeToken<List<WordModel>>() {}.type
        return Gson().fromJson(jsonArray.toString(), type)
    }

    private fun saveIdToPreferences(wordId: Int) {
        val knownWordIds = getKnownWordIds().toMutableSet()
        knownWordIds.add(wordId)
        val editor = sharedPreferences.edit()
        editor.putStringSet("knownWordIds", knownWordIds.map { it.toString() }.toSet())
        editor.apply()
    }

    private fun getKnownWordIds(): Set<Int> {
        return sharedPreferences.getStringSet("knownWordIds", emptySet())
            ?.mapNotNull { it.toIntOrNull() }
            ?.toSet() ?: emptySet()
    }

    private fun findWord(id: Int): WordModel {
        return wordList.find { it.id == id }!!
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}