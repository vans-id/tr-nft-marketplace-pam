package com.djevannn.nftmarketplace.ui.main.user_nft

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.djevannn.nftmarketplace.ViewModelFactory
import com.djevannn.nftmarketplace.adapters.nft.ListNFTAdapter
import com.djevannn.nftmarketplace.adapters.nft.OnItemClickCallback
import com.djevannn.nftmarketplace.data.NFT
import com.djevannn.nftmarketplace.data.User
import com.djevannn.nftmarketplace.data.UserRegist
import com.djevannn.nftmarketplace.databinding.ActivityNftuserBinding
import com.djevannn.nftmarketplace.helper.UserPreference
import com.djevannn.nftmarketplace.ui.auth.login.LoginActivity
import com.djevannn.nftmarketplace.ui.main.detail.DetailActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "settings"
)

class NFTUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNftuserBinding
    private lateinit var viewModel: NFTUserViewModel
    private lateinit var user: User
    private lateinit var username: String
    private lateinit var data: UserRegist
    val listNFTAdapter = ListNFTAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNftuserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = UserRegist("", 0.0, "", "", "", "", "", "")
        username = intent.getStringExtra("username").toString()

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupAction() {
        listNFTAdapter.setOnItemClickCallback(object :
            OnItemClickCallback {
            override fun onItemClicked(data: NFT) {
                val intent = Intent(
                    this@NFTUserActivity,
                    DetailActivity::class.java
                )
                intent.putExtra("DATA", data)
                startActivity(intent)
            }
        })
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[NFTUserViewModel::class.java]
        viewModel.getUserNFT(username)
        viewModel.fetchAllCollection(username)
        viewModel.getUser().observe(this) {
            this.user = it
            if (!it.isLogin) {
                startActivity(
                    Intent(
                        this@NFTUserActivity,
                        LoginActivity::class.java
                    )
                )
            }
        }

        viewModel.userNFT.observe(this) {
            data = it
            val front = it.wallet.take(6)
            val back = it.wallet.takeLast(4)
            val truncate = "$front...$back"

            binding.apply {
                tvWallet.text = it.wallet
                tvName.text = it.name
                tvWallet.text = truncate
                tvAbout.text = it.about
                Glide.with(root)
                    .load(it.photo_url)
                    .apply(RequestOptions().override(300, 300))
                    .into(ivAvatar)
            }
        }


        with(binding.rvCollection) {
            layoutManager =
                LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = listNFTAdapter
            isNestedScrollingEnabled = false
        }

        viewModel.collectionList.observe(this) {
            listNFTAdapter.setNFTs(it)
            listNFTAdapter.notifyDataSetChanged()
        }

    }

    private fun setupView() {
        title = "Owner"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}