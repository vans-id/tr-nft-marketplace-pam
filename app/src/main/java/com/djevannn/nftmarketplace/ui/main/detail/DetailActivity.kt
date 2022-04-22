package com.djevannn.nftmarketplace.ui.main.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.djevannn.nftmarketplace.R
import com.djevannn.nftmarketplace.ViewModelFactory
import com.djevannn.nftmarketplace.data.NFT
import com.djevannn.nftmarketplace.databinding.ActivityDetailBinding
import com.djevannn.nftmarketplace.helper.UserPreference
import com.djevannn.nftmarketplace.helper.formatNumber
import com.djevannn.nftmarketplace.ui.main.listings.ListingActivity
import com.djevannn.nftmarketplace.ui.main.user_nft.NFTUserActivity


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "settings"
)

class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<NFT>("DATA")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[DetailViewModel::class.java]

        if (data != null) {
            viewModel.getCreatorData(data.creator)
            viewModel.getFavoriteStatus(data)
            viewModel.checkMine(data)

            Glide.with(this@DetailActivity)
                .load(data.image_url)
                .into(binding.ivDetailImage)

            val nftTitle = data.title + data.token_id

            supportActionBar?.title = nftTitle

            binding.tvDetailTitle.text = nftTitle
            binding.tvDetailPrice.text = "${formatNumber(data.current_price)} ETH"
            binding.tvDetailOwner.text = data.owner

            binding.tvDetailOwner.setOnClickListener {
                val intent = Intent(this@DetailActivity,NFTUserActivity::class.java)
                intent.putExtra("username", data.owner)
                startActivity(intent)
            }

            binding.btnBuyNow.setOnClickListener {
                showConfirmDialog(data)
            }
            binding.fabDetail.setOnClickListener {
                viewModel.onFavoriteClicked(data)
            }
            binding.cvHistory.setOnClickListener {
                val intent =
                    Intent(
                        this@DetailActivity,
                        ListingActivity::class.java
                    )
                intent.putExtra("TOKEN_ID", data.token_id)
                startActivity(intent)
            }

            viewModel.isMine.observe(this@DetailActivity) {
                val visibility = when (it) {
                    true -> View.GONE
                    false -> View.VISIBLE
                }

                binding.cvBuyNow.visibility = visibility
                binding.btnBuyNow.visibility = visibility
            }
            viewModel.creator.observe(this@DetailActivity) {
                binding.tvDetailAbout.text = it.about
                binding.tvDetailDescription.text =
                    "Created by ${it.name}"
                Glide.with(this@DetailActivity)
                    .load(it.photo_url)
                    .into(binding.ivDetailCreatorImage)
            }
            viewModel.isLoading.observe(this@DetailActivity) {
                binding.pbDetail.visibility = when (it) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            }

            viewModel.isFavorite.observe(this@DetailActivity) {
                val image = when (it) {
                    true -> R.drawable.ic_baseline_favorite_24
                    false -> R.drawable.ic_baseline_favorite_border_24
                }
                binding.fabDetail.setImageResource(image)
            }

            viewModel.message.observe(this@DetailActivity) {
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
        val view =
            layoutInflater.inflate(R.layout.dialog_checkout, null)
        val tvTitle =
            view.findViewById<TextView>(R.id.tv_confirm_title)
        val tvPrice =
            view.findViewById<TextView>(R.id.tv_confirm_price)
        val btnConfirm = view.findViewById<Button>(R.id.btn_confirm)
        val btnCancel = view.findViewById<Button>(R.id.btn_cancel)

        builder.setView(view)

        tvTitle.text = data.title + data.token_id
        tvPrice.text = "${formatNumber(data.current_price)} ETH"
        btnConfirm.setOnClickListener {
            viewModel.buyNFT(data)
            builder.dismiss()
            finish()
        }
        btnCancel.setOnClickListener {
            builder.dismiss()
        }

        builder.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}