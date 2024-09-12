package com.ziyad.wordup

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ziyad.wordup.databinding.FragmentLearningWordsBinding
import com.ziyad.wordup.recyclerview.WordAdapter
import com.ziyad.wordup.recyclerview.WordModel
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStreamReader
import java.util.Collections

class LearningWordsFragment : Fragment() {

    private var _binding: FragmentLearningWordsBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var wordAdapter: WordAdapter
    private lateinit var wordList: List<WordModel>
    private lateinit var learningWords : List<WordModel>
    private lateinit var jsonString: String
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var sharedPreferences: SharedPreferences
    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == "knownWordIds") {
            loadData()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLearningWordsBinding.inflate(inflater, container, false)
        val activity = requireActivity()
        activity.findViewById<BottomNavigationView>(R.id.bottomNavigation).visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", 0)
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener)

        recyclerView = binding.recyclerViewLearning
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        swipeRefreshLayout = binding.swipeToRefresh
        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        loadData()
    }

    private fun loadData() {
        val inputStream = context?.resources?.openRawResource(R.raw.words)
        if(inputStream != null){
            jsonString = InputStreamReader(inputStream).use { it.readText() }
        }
        val jsonObject = JSONObject(jsonString)
        val jsonArray = jsonObject.getJSONArray("words")
        val type = object : TypeToken<List<WordModel>>() {}.type
        wordList = Gson().fromJson(jsonArray.toString(), type)

        learningWords = wordList.filterNot { it.id in getKnownWordIds() }
        Collections.shuffle(learningWords)

        wordAdapter = WordAdapter(learningWords){ word ->
            findNavController()
                .navigate(LearningWordsFragmentDirections.actionLearningWordsFragmentToWordMeaningDialogFragment(word.id))
        }
        recyclerView.adapter = wordAdapter
    }

    private fun refreshData() {
        loadData()
        swipeRefreshLayout.isRefreshing = false
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