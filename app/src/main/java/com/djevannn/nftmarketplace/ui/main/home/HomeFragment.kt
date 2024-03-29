package com.djevannn.nftmarketplace.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.djevannn.nftmarketplace.adapters.nft.ListNFTAdapter
import com.djevannn.nftmarketplace.adapters.nft.OnItemClickCallback
import com.djevannn.nftmarketplace.data.NFT
import com.djevannn.nftmarketplace.databinding.FragmentHomeBinding
import com.djevannn.nftmarketplace.ui.main.detail.DetailActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        val listNFTAdapter = ListNFTAdapter()
        with(binding.rvNfts) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = listNFTAdapter
        }

        viewModel.fetchAllNFT()
        viewModel.listNft.observe(viewLifecycleOwner) {
            listNFTAdapter.setNFTs(it)
            listNFTAdapter.notifyDataSetChanged()
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.pbHome.visibility = when (it) {
                true -> View.VISIBLE
                false -> View.GONE
            }
        }

        listNFTAdapter.setOnItemClickCallback(object :
            OnItemClickCallback {
            override fun onItemClicked(data: NFT) {
                val intent =
                    Intent(context, DetailActivity::class.java)
                intent.putExtra("DATA", data)
                startActivity(intent)
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}