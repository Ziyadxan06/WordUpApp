package com.ziyad.wordup

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ziyad.wordup.databinding.FragmentKnownWordMeaningDialogBinding
import com.ziyad.wordup.databinding.FragmentWordMeaningDialogBinding
import com.ziyad.wordup.recyclerview.WordModel
import org.json.JSONObject
import java.io.InputStreamReader

class KnownWordMeaningDialogFragment : DialogFragment() {

    private var _binding: FragmentKnownWordMeaningDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var word: WordModel
    private lateinit var wordList : List<WordModel>
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentKnownWordMeaningDialogBinding.inflate(inflater, container, false)

        val wordId = KnownWordMeaningDialogFragmentArgs.fromBundle(requireArguments()).knownWordId
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", 0)
        wordList = loadWordList()
        word = findWord(wordId)

        binding.wordTextView.text = word.word
        binding.pronunciationTextView.text = word.pronunciation
        binding.turkishTextView.text = word.turkish
        binding.azerbaijaniTextView.text = word.azerbaijani

        Glide.with(this)
            .load(word.imageUri)
            .into(binding.wordImage)

        binding.btnLearn.setOnClickListener {
            val wordId = word.id
            if (wordId != -1) {
                removeIdFromPreferences(wordId)
                dismiss()
            }
            Toast.makeText(context, "${word.word} removed from known words", Toast.LENGTH_SHORT).show()
        }

        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun loadWordList(): List<WordModel> {
        val inputStream = context?.resources?.openRawResource(R.raw.words)
        val jsonString = InputStreamReader(inputStream).use { it.readText() }
        val jsonObject = JSONObject(jsonString)
        val jsonArray = jsonObject.getJSONArray("words")
        val type = object : TypeToken<List<WordModel>>() {}.type
        return Gson().fromJson(jsonArray.toString(), type)
    }

    private fun removeIdFromPreferences(wordId: Int) {
        val knownWordIds = getKnownWordIds().toMutableSet()
        knownWordIds.remove(wordId)
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