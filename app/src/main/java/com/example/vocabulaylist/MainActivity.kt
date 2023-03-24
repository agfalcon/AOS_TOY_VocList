package com.example.vocabulaylist

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vocabulaylist.databinding.ActivityMainBinding
import com.google.android.material.chip.Chip

class MainActivity : AppCompatActivity(), WordAdapter.ItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var wordAdapter: WordAdapter
    private var selectedWord : Word? = null
    private val updateAddWordResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        val isUpdated = result.data?.getBooleanExtra("isUpdated", false) ?: false
        if(result.resultCode == RESULT_OK && isUpdated){
            updateAddWord()
        }
    }


    private val updateEditWordResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        val editWord = result.data?.getParcelableExtra<Word>("updatedWord") ?: null
        if(result.resultCode == RESULT_OK && editWord != null){
            updateEditWord(editWord)

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

        binding.addButton.setOnClickListener {
            Intent(this, AddActivity::class.java).let{
                updateAddWordResult.launch(it)
            }
        }

        binding.deleteImageView.setOnClickListener{
            delete()
        }

        binding.editImageView.setOnClickListener {
            edit()
        }
    }


    private fun initRecyclerView(){

        wordAdapter = WordAdapter(mutableListOf(), this)

        binding.wordRecyclerView.apply{
            adapter = wordAdapter
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            val dividerItemDecoration = DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)
            addItemDecoration(dividerItemDecoration)
        }

        Thread{
            val list = AppDatabase.getInstance(this)?.wordDao()?.getAll() ?: emptyList()
            wordAdapter.list.addAll(list)
            runOnUiThread {
                wordAdapter.notifyDataSetChanged()
            }
        }.start()
    }

    private fun updateAddWord() {
        Thread{
            AppDatabase.getInstance(this)?.wordDao()?.getLatestWord()?.let{
                wordAdapter.list.add(0,  it)
                runOnUiThread {
                    wordAdapter.notifyDataSetChanged()
                }
            }
        }.start()
    }

    private fun updateEditWord(editWord: Word){
        val index = wordAdapter.list.indexOfFirst { it.id == editWord.id }
        wordAdapter.list.set(index, editWord)
        runOnUiThread {
            selectedWord = editWord
            binding.textTextView.text = editWord.text
            binding.meanTextView.text = editWord.mean
            wordAdapter.notifyItemChanged(index) }
    }

    private fun delete(){
        if(selectedWord ==null) return
        Thread{
            selectedWord?.let{ word ->
                AppDatabase.getInstance(this)?.wordDao()?.delete(word)
                runOnUiThread {
                    wordAdapter.list.remove(word)
                    wordAdapter.notifyDataSetChanged()
                    binding.textTextView.text = ""
                    binding.meanTextView.text = ""
                }
            }
        }.start()
    }

    private fun edit(){
        if(selectedWord == null) return
        val intent = Intent(this, AddActivity::class.java).putExtra("originData", selectedWord)
        updateEditWordResult.launch(intent)
    }

    override fun onClick(word: Word) {
        selectedWord = word
        binding.textTextView.text = word.text
        binding.meanTextView.text = word.mean
    }

    override fun onClick(chip: Chip) {
        Toast.makeText(this, "${chip.text}가 클릭", Toast.LENGTH_SHORT).show()
    }
}