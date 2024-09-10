package com.ziyad.wordup.recyclerview

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ziyad.wordup.R

class WordViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    val wordTextView : TextView = itemView.findViewById(R.id.wordTextView)
    val pronunciationTextView : TextView = itemView.findViewById(R.id.pronunciationTextView)
}