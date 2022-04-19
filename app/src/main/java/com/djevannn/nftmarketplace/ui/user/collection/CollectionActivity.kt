package com.djevannn.nftmarketplace.ui.user.collection

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
import com.djevannn.nftmarketplace.databinding.ActivityCollectionBinding
import com.djevannn.nftmarketplace.helper.UserPreference
import com.djevannn.nftmarketplace.ui.main.detail.DetailActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "settings"
)

class CollectionActivity : AppCompatActivity() {

    private lateinit var viewModel: CollectionViewModel
    private lateinit var binding: ActivityCollectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Collection"

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[CollectionViewModel::class.java]

        val listNFTAdapter = ListNFTAdapter()
        with(binding.rvCollection) {
            layoutManager =
                GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = listNFTAdapter
        }

        viewModel.collectionList.observe(this) {
            listNFTAdapter.setNFTs(it)
            listNFTAdapter.notifyDataSetChanged()
        }
        viewModel.isLoading.observe(this) {
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}