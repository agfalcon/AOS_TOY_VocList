package com.example.vocabulaylist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vocabulaylist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), WordAdapter.ItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var wordAdapter: WordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

        binding.addButton.setOnClickListener {
            Intent(this, AddActivity::class.java).let{
                startActivity(it)
            }
        }
    }



    private fun initRecyclerView(){
        val dummyList = mutableListOf<Word>(
            Word("weather", "날씨", "명사"),
            Word("run", "달리다", "동사사"),
            Word("honey", "꿀", "명사"),
            Word("like", "좋아하다", "동사"),
        )
        wordAdapter = WordAdapter(dummyList, this)

        binding.wordRecyclerView.apply{
            adapter = wordAdapter
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            val dividerItemDecoration = DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)
            addItemDecoration(dividerItemDecoration)
        }
    }

    override fun onClick(word: Word) {
        Toast.makeText(this, "${word.text}가 클릭", Toast.LENGTH_SHORT).show()
    }
}