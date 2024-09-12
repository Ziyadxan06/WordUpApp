package com.ziyad.wordup.recyclerview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ziyad.wordup.R

class MasteredWordAdapter(val wordList: List<WordModel>, val onItemClick: (WordModel) -> Unit) : RecyclerView.Adapter<WordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_layout, parent, false)
        val wordViewHolder = WordViewHolder(itemView)
        return wordViewHolder
    }

    override fun getItemCount(): Int {
        return wordList.size
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = wordList[position]
        holder.wordTextView.text = word.word
        holder.pronunciationTextView.text = word.pronunciation

        holder.itemView.setOnClickListener {
            onItemClick(word)
        }
    }
}