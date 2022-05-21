package com.example.googletranslate.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.googletranslate.R
import com.example.googletranslate.adapter.helper.ItemTouchHelperAdapter
import com.example.googletranslate.model.SaveTranslate
import com.example.googletranslate.ui.history.HistoryActivity

class HistoryAdapter(var context: HistoryActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemTouchHelperAdapter{

    var items: ArrayList<SaveTranslate> = ArrayList()

    inner class HistoryViewHolder(view: View): RecyclerView.ViewHolder(view){
        var ll_item_click: LinearLayout
        var tv_from_text: TextView
        var tv_to_text: TextView
        var iv_star: ImageView

        init {
            ll_item_click = view.findViewById(R.id.ll_item_click)
            tv_from_text = view.findViewById(R.id.tv_from_text)
            tv_to_text = view.findViewById(R.id.tv_to_text)
            iv_star = view.findViewById(R.id.iv_star)
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(item: SaveTranslate){
            tv_from_text.text = item.textFrom
            tv_to_text.text = item.textTo
            ll_item_click.setOnClickListener {
                context.sendResultSerializable(item)
            }
            iv_star.setOnClickListener {
                if (iv_star.drawable.constantState == context.resources.getDrawable(R.drawable.ic_empty_star).constantState){
                    iv_star.setImageResource(R.drawable.ic_full_star)
                }else{
                    iv_star.setImageResource(R.drawable.ic_empty_star)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_history_view, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HistoryViewHolder){
            holder.bind(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onItemDismiss(position: Int) {
        context.viewModel.removeItem(items[position])
        items.removeAt(position)
        notifyItemRemoved(position)
        Toast.makeText(context, "This Translation has been successfully deleted!", Toast.LENGTH_SHORT).show()
    }


}