package com.ziyad.wordup

import android.content.Intent
import android.content.Intent.getIntent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ziyad.wordup.databinding.FragmentMasteredWordsBinding
import com.ziyad.wordup.recyclerview.MasteredWordAdapter
import com.ziyad.wordup.recyclerview.WordAdapter
import com.ziyad.wordup.recyclerview.WordModel
import org.json.JSONObject
import java.io.InputStreamReader
import java.util.Collections

class MasteredWordsFragment : Fragment() {
    private var _binding: FragmentMasteredWordsBinding? = null
    private val binding get() = _binding!!

    private lateinit var masteredRecyclerView: RecyclerView
    private lateinit var masteredWordAdapter: MasteredWordAdapter
    private lateinit var masteredWordList: List<WordModel>
    private lateinit var sharedPreferences: SharedPreferences
    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == "knownWordIds") {
            // Eğer knownWordIds değişirse loadData() fonksiyonunu tetikleyin
            loadData()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMasteredWordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        masteredRecyclerView = binding.masteredRecyclerView
        masteredRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", 0)
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener)

        loadData()
    }

    private fun loadData() {
        val knownWordIds = getKnownWordIds()
        val inputStream = context?.resources?.openRawResource(R.raw.words)
        val jsonString = InputStreamReader(inputStream).use { it.readText() }
        val jsonObject = JSONObject(jsonString)
        val jsonArray = jsonObject.getJSONArray("words")
        val type = object : TypeToken<List<WordModel>>() {}.type
        masteredWordList = Gson().fromJson(jsonArray.toString(), type)

        val knownWords = masteredWordList.filter { it.id in knownWordIds }

        masteredWordAdapter = MasteredWordAdapter(knownWords){ knownWord ->
            findNavController()
                .navigate(MasteredWordsFragmentDirections.actionMasteredWordsFragmentToKnownWordMeaningDialogFragment(knownWord.id))
        }
        masteredRecyclerView.adapter = masteredWordAdapter
    }

    private fun getKnownWordIds(): Set<Int> {
        return sharedPreferences.getStringSet("knownWordIds", emptySet())
            ?.mapNotNull { it.toIntOrNull() }
            ?.toSet() ?: emptySet()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}