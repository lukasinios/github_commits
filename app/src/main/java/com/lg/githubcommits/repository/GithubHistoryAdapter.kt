package com.lg.githubcommits.repository

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lg.domain.history.view.CachedRepositoriesViewData
import com.lg.githubcommits.databinding.ItemHistoryBinding

class GithubHistoryAdapter(private val onClickItem: (cachedRepositoriesViewData: CachedRepositoriesViewData) -> Unit) :
    RecyclerView.Adapter<GithubHistoryAdapter.HistoryViewHolder>() {

    var items: MutableList<CachedRepositoriesViewData> = mutableListOf()
        set(value) {
            field.addAll(value)
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HistoryViewHolder(
            ItemHistoryBinding.inflate(layoutInflater, parent, false),
            onClickItem
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class HistoryViewHolder(
        private val itemHistoryBinding: ItemHistoryBinding,
        private val onClickItem: (cachedRepositoriesViewData: CachedRepositoriesViewData) -> Unit
    ) : RecyclerView.ViewHolder(itemHistoryBinding.root) {
        fun bind(cachedRepositoriesViewData: CachedRepositoriesViewData) {
            itemHistoryBinding.ownerAndRepository.text =
                cachedRepositoriesViewData.ownerAndRepository
            itemHistoryBinding.root.setOnClickListener {
                onClickItem(cachedRepositoriesViewData)
            }
        }

    }
}