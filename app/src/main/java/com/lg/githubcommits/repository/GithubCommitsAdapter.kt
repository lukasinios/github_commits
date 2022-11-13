package com.lg.githubcommits.repository

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lg.domain.githublist.view.CommitsViewData
import com.lg.githubcommits.R
import com.lg.githubcommits.databinding.ItemCommitBinding

class GithubCommitsAdapter() :
    RecyclerView.Adapter<GithubCommitsAdapter.CommitsViewHolder>() {

    var items: MutableList<CommitsViewData> = mutableListOf()
        set(value) {
            field.addAll(value)
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommitsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CommitsViewHolder(
            ItemCommitBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CommitsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class CommitsViewHolder(
        private val itemCommitBinding: ItemCommitBinding
    ) : RecyclerView.ViewHolder(itemCommitBinding.root) {

        fun bind(commitsViewData: CommitsViewData) {
            itemCommitBinding.selectCommit.isChecked = commitsViewData.isSelected
            itemCommitBinding.selectCommit.setOnClickListener {
                commitsViewData.isSelected = itemCommitBinding.selectCommit.isChecked
            }
            itemCommitBinding.message.text =
                itemCommitBinding.root.context.getString(R.string.message, commitsViewData.message)
            itemCommitBinding.author.text = itemCommitBinding.root.context.getString(
                R.string.author,
                commitsViewData.authorName
            )
            itemCommitBinding.sha.text =
                itemCommitBinding.root.context.getString(R.string.sha, commitsViewData.sha)
        }

    }
}