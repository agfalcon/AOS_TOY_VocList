package com.example.vocabulaylist

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabulaylist.databinding.ItemWordBinding
import com.google.android.material.chip.Chip

class WordAdapter(
    private val list: MutableList<Word>,
    private val itemClickListener: ItemClickListener? = null
    ): RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemWordBinding.inflate(inflater, parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = list[position]
        holder.bind(word)
        holder.itemView.setOnClickListener {
            itemClickListener?.onClick(word)
        }
        holder.binding.typeChip.setOnClickListener {
            itemClickListener?.onClick(holder.binding.typeChip)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class WordViewHolder(val binding: ItemWordBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(word: Word){
            binding.apply {
                textTextView.text = word.text
                meanTextView.text = word.mean
                typeChip.text = word.type
            }
        }
    }

    interface ItemClickListener{
        fun onClick(word: Word)
        fun onClick(chip: Chip)
    }
}