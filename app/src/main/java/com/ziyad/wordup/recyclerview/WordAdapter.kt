package com.ziyad.wordup.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ziyad.wordup.R

class WordAdapter(val wordList: List<WordModel>) : RecyclerView.Adapter<WordViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_layout, parent, false)
        val wordViewHolder = WordViewHolder(itemView)
        return wordViewHolder
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = wordList[position]
        holder.wordTextView.text = word.word
        holder.pronunciationTextView.text = word.pronunciation
    }

    override fun getItemCount(): Int {
        return wordList.size
    }
}