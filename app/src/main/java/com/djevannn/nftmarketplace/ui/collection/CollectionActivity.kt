package com.djevannn.nftmarketplace.ui.collection

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.djevannn.nftmarketplace.adapters.ListNFTAdapter
import com.djevannn.nftmarketplace.adapters.OnItemClickCallback
import com.djevannn.nftmarketplace.data.NFT
import com.djevannn.nftmarketplace.databinding.ActivityCollectionBinding
import com.djevannn.nftmarketplace.ui.detail.DetailActivity

class CollectionActivity : AppCompatActivity() {

    private val collectionViewModel: CollectionViewModel by viewModels()
    private lateinit var binding: ActivityCollectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Koleksi"

        val listNFTAdapter = ListNFTAdapter()
        with(binding.rvCollection) {
            layoutManager =
                GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = listNFTAdapter
        }

        collectionViewModel.collectionList.observe(this) {
            listNFTAdapter.setNFTs(it)
            listNFTAdapter.notifyDataSetChanged()
        }
        collectionViewModel.isLoading.observe(this) {
            binding.pbCollection.visibility = when (it) {
                true -> View.VISIBLE
                false -> View.GONE
            }
        }
        listNFTAdapter.setOnItemClickCallback(object :
            OnItemClickCallback {
            override fun onItemClicked(data: NFT) {
                val intent = Intent(
                    this@CollectionActivity,
                    DetailActivity::class.java
                )
                intent.putExtra("DATA", data)
                startActivity(intent)
            }
        })
    }
}