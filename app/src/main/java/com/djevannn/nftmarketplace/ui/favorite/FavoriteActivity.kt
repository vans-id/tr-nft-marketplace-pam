package com.djevannn.nftmarketplace.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.djevannn.nftmarketplace.adapters.ListNFTAdapter
import com.djevannn.nftmarketplace.adapters.OnItemClickCallback
import com.djevannn.nftmarketplace.data.NFT
import com.djevannn.nftmarketplace.databinding.ActivityFavoriteBinding
import com.djevannn.nftmarketplace.ui.detail.DetailActivity

class FavoriteActivity : AppCompatActivity() {

    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Favorit"

        val listNFTAdapter = ListNFTAdapter()
        with(binding.rvFavorite) {
            layoutManager =
                GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = listNFTAdapter
        }

        favoriteViewModel.favoriteList.observe(this) {
            listNFTAdapter.setNFTs(it)
            listNFTAdapter.notifyDataSetChanged()
        }
        favoriteViewModel.isLoading.observe(this) {
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
}