package com.djevannn.nftmarketplace.ui.favorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.djevannn.nftmarketplace.ViewModelFactory
import com.djevannn.nftmarketplace.adapters.nft.ListNFTAdapter
import com.djevannn.nftmarketplace.adapters.nft.OnItemClickCallback
import com.djevannn.nftmarketplace.data.NFT
import com.djevannn.nftmarketplace.databinding.ActivityFavoriteBinding
import com.djevannn.nftmarketplace.helper.UserPreference
import com.djevannn.nftmarketplace.ui.detail.DetailActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "settings"
)

class FavoriteActivity : AppCompatActivity() {

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Favorit"

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[FavoriteViewModel::class.java]

        val listNFTAdapter = ListNFTAdapter()
        with(binding.rvFavorite) {
            layoutManager =
                GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = listNFTAdapter
        }

        viewModel.favoriteList.observe(this) {
            listNFTAdapter.setNFTs(it)
            listNFTAdapter.notifyDataSetChanged()
        }
        viewModel.isLoading.observe(this) {
            binding.pbFavorite.visibility = when (it) {
                true -> View.VISIBLE
                false -> View.GONE
            }
        }
        listNFTAdapter.setOnItemClickCallback(object :
            OnItemClickCallback {
            override fun onItemClicked(data: NFT) {
                val intent = Intent(
                    this@FavoriteActivity,
                    DetailActivity::class.java
                )
                intent.putExtra("DATA", data)
                startActivity(intent)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}