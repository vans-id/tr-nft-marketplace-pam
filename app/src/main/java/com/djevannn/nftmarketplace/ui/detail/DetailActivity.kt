package com.djevannn.nftmarketplace.ui.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.djevannn.nftmarketplace.data.NFT
import com.djevannn.nftmarketplace.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private val detailViewModel: DetailViewModel by viewModels()
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<NFT>("DATA")

        supportActionBar?.title = data?.title ?: "Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (data != null) {
            detailViewModel.getCreatorData(data.creator)

            Glide.with(this@DetailActivity)
                .load(data.image_url)
                .into(binding.ivDetailImage)

            binding.tvDetailTitle.text = data.title
            binding.tvDetailPrice.text = "${data.current_price} ETH"

            detailViewModel.creator.observe(this@DetailActivity) {
                binding.tvDetailAbout.text = it.about
                binding.tvDetailDescription.text = "Created by ${it.name}"
            }
            detailViewModel.isLoading.observe(this@DetailActivity) {
                binding.pbDetail.visibility = when (it) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            }
        }
    }
}