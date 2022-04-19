package com.djevannn.nftmarketplace.ui.main.listings

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.djevannn.nftmarketplace.adapters.listing.ListingAdapter
import com.djevannn.nftmarketplace.databinding.ActivityListingBinding

class ListingActivity : AppCompatActivity() {

    private val viewModel: ListingViewModel by viewModels()
    private lateinit var binding: ActivityListingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Listings"

        val listingAdapter = ListingAdapter()
        with(binding.rvListings) {
            layoutManager =
                LinearLayoutManager(
                    context
                )
            setHasFixedSize(true)
            adapter = listingAdapter
            addItemDecoration(
                DividerItemDecoration(
                    baseContext,
                    LinearLayoutManager.VERTICAL
                )
            )
        }

        val tokenId = intent.getIntExtra("TOKEN_ID", 0)

        viewModel.fetchNFTListings(tokenId)
        viewModel.listings.observe(this) {
            listingAdapter.setNFTs(it)
            listingAdapter.notifyDataSetChanged()
        }
        viewModel.isLoading.observe(this) {
            binding.pbListing.visibility = when (it) {
                true -> View.VISIBLE
                false -> View.GONE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}