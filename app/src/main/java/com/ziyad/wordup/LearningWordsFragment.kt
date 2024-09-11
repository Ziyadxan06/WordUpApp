package com.ziyad.wordup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLearningWordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        val jsonString = InputStreamReader(inputStream).use { it.readText() }
        val jsonObject = JSONObject(jsonString)
        val jsonArray = jsonObject.getJSONArray("words")
        val type = object : TypeToken<List<WordModel>>() {}.type
        wordList = Gson().fromJson(jsonArray.toString(), type)

        Collections.shuffle(wordList)

        wordAdapter = WordAdapter(wordList)
        recyclerView.adapter = wordAdapter
    }

    private fun refreshData() {
        loadData()
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}