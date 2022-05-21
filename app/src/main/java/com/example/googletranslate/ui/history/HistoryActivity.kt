package com.example.googletranslate.ui.history

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.googletranslate.R
import com.example.googletranslate.adapter.HistoryAdapter
import com.example.googletranslate.adapter.helper.ItemTouchHelperCallback
import com.example.googletranslate.databinding.ActivityHistoryBinding
import com.example.googletranslate.model.SaveTranslate
import com.example.googletranslate.utils.UiStateList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    val viewModel by viewModels<HistoryViewModel>()
    private lateinit var adapter: HistoryAdapter
    private val TAG = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setupObservers()
        initViews()

    }

    private fun initViews() {
        binding.rvHistory.layoutManager = GridLayoutManager(this, 1)
        adapter = HistoryAdapter(this)
        viewModel.getAllTranslate()
        binding.rvHistory.adapter = adapter

        val callback = ItemTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.rvHistory)  // // Drag on RecyclerView Item

        binding.ivBack.setOnClickListener { finish() }
    }

    private fun setupObservers(){
        lifecycleScope.launchWhenCreated {
            viewModel.savedListState.collect{
                when(it){
                    is UiStateList.LOADING -> {}
                    is UiStateList.SUCCESS -> {
                        adapter.items = it.data as ArrayList<SaveTranslate>
                    }
                    is UiStateList.ERROR -> {
                        Log.d(TAG, it.message) }
                    else -> Unit
                }
            }
        }
    }

    fun sendResultSerializable(saveTranslate: SaveTranslate) {
        val intent = Intent()
        intent.putExtra("result", saveTranslate)
        setResult(RESULT_OK, intent)
        finish()
    }

}