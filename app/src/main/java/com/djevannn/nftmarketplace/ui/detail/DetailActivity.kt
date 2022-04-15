package com.djevannn.nftmarketplace.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.djevannn.nftmarketplace.R
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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (data != null) {
            detailViewModel.getCreatorData(data.creator)
            detailViewModel.getFavoriteStatus(data)

            Glide.with(this@DetailActivity)
                .load(data.image_url)
                .into(binding.ivDetailImage)

            val nftTitle = data.title + data.token_id

            supportActionBar?.title = nftTitle

            binding.tvDetailTitle.text = nftTitle
            binding.tvDetailPrice.text = "${data.current_price} ETH"
            binding.tvDetailOwner.text = data.owner

            binding.btnBuyNow.setOnClickListener {
                showConfirmDialog(data)
            }
            binding.fabDetail.setOnClickListener {
                detailViewModel.onFavoriteClicked(data)
            }

            detailViewModel.creator.observe(this@DetailActivity) {
                binding.tvDetailAbout.text = it.about
                binding.tvDetailDescription.text =
                    "Created by ${it.name}"
                Glide.with(this@DetailActivity)
                    .load(it.image_url)
                    .into(binding.ivDetailCreatorImage)
            }
            detailViewModel.isLoading.observe(this@DetailActivity) {
                binding.pbDetail.visibility = when (it) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            }
            detailViewModel.isFavorite.observe(this@DetailActivity) {
                val image = when (it) {
                    true -> R.drawable.ic_baseline_favorite_24
                    false -> R.drawable.ic_baseline_favorite_border_24
                }
                binding.fabDetail.setImageResource(image)
            }
            detailViewModel.message.observe(this@DetailActivity) {
                Toast.makeText(
                    this@DetailActivity,
                    it.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showConfirmDialog(data: NFT) {
        val builder = AlertDialog.Builder(this, 0).create()
        val view = layoutInflater.inflate(R.layout.custom_dialog, null)
        val tvTitle = view.findViewById<TextView>(R.id.tv_confirm_title)
        val tvPrice = view.findViewById<TextView>(R.id.tv_confirm_price)
        val btnConfirm = view.findViewById<Button>(R.id.btn_confirm)
        val btnCancel = view.findViewById<Button>(R.id.btn_cancel)

        builder.setView(view)

        tvTitle.text = data.title + data.token_id
        tvPrice.text = "${data.current_price} ETH"
        btnConfirm.setOnClickListener {
            detailViewModel.buyNFT(data)
            builder.dismiss()
        }
        btnCancel.setOnClickListener {
            builder.dismiss()
        }

        builder.show()

    }
}