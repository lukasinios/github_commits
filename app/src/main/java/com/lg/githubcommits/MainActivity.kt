package com.lg.githubcommits

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.lg.domain.githublist.view.RepositoryCommitsViewData
import com.lg.domain.history.view.CachedRepositoriesViewData
import com.lg.githubcommits.databinding.ActivityMainBinding
import com.lg.githubcommits.repository.GithubCommitsAdapter
import com.lg.githubcommits.repository.GithubHistoryAdapter
import com.lg.githubcommits.repository.GithubRepositoryEvents
import com.lg.githubcommits.repository.GithubRepositoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<GithubRepositoryViewModel>()

    private val githubHistoryAdapter by lazy {
        GithubHistoryAdapter() {
            viewModel.loadRepository(it.ownerAndRepository)
        }
    }
    private val githubCommitAdapter by lazy { GithubCommitsAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.historyRecycler.adapter = githubHistoryAdapter
        binding.commitsRecycler.adapter = githubCommitAdapter

        observeState()
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.loadRepository(query.orEmpty().trim())
                hideHistory()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        binding.sendButton.setOnClickListener {
            viewModel.shareSelectedCommits(githubCommitAdapter.items)
        }
        viewModel.loadHistory()

        binding.commitsRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    viewModel.loadNextPage()
                }
            }
        })
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect() {
                    when (it) {
                        GithubRepositoryEvents.InitialState -> {
                            //do nothing
                        }
                        GithubRepositoryEvents.ShowLoading -> showProgress()
                        is GithubRepositoryEvents.ShowCachedHistory -> showCachedHistory(it.cachedHistory)
                        is GithubRepositoryEvents.ShowFetchedRepositoryCommits -> showFetchedCommits(
                            it.repositoryCommitsViewData,
                            it.clearAdapter
                        )
                        is GithubRepositoryEvents.ShowToastError -> showError(it)
                        is GithubRepositoryEvents.ShareSelectedCommits -> sendDataToOtherApp(it.commitsToShare)
                    }
                }
            }

        }
    }

    private fun sendDataToOtherApp(commitsToShare: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, commitsToShare)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun showError(it: GithubRepositoryEvents.ShowToastError) {
        hideProgress()
        Toast.makeText(
            this,
            it.message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showFetchedCommits(
        repositoryCommitsViewData: RepositoryCommitsViewData,
        clearAdapter: Boolean
    ) {
        hideProgress()
        hideHistory()
        showCommits()
        hideKeyboard()
        binding.repositoryId.text =
            getString(R.string.repository_id, "${repositoryCommitsViewData.id}")
        if (clearAdapter) {
            githubCommitAdapter.items.clear()
        }
        githubCommitAdapter.items = repositoryCommitsViewData.commitsViewsList.toMutableList()
    }

    fun hideKeyboard(){
        val view = this.currentFocus
        if (view != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showCachedHistory(cachedHistory: List<CachedRepositoriesViewData>) {
        hideProgress()
        githubHistoryAdapter.items = cachedHistory.toMutableList()
    }

    private fun hideHistory() {
        binding.historyRecycler.visibility = View.GONE
    }

    private fun showCommits() {
        binding.commitsRecycler.visibility = View.VISIBLE
        binding.sendButton.visibility = View.VISIBLE
        binding.repositoryId.visibility = View.VISIBLE
    }

    private fun showProgress() {
        binding.progress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.progress.visibility = View.GONE
    }
}