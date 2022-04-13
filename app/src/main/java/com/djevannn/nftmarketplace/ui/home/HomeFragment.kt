package com.djevannn.nftmarketplace.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.djevannn.nftmarketplace.adapters.ListNFTAdapter
import com.djevannn.nftmarketplace.adapters.OnItemClickCallback
import com.djevannn.nftmarketplace.data.NFT
import com.djevannn.nftmarketplace.databinding.FragmentHomeBinding
import com.djevannn.nftmarketplace.ui.detail.DetailActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        val listNFTAdapter = ListNFTAdapter()
        with(binding.rvNfts) {
            layoutManager =
                GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = listNFTAdapter
        }

        homeViewModel.fetchAllNFT()
        homeViewModel.listNft.observe(viewLifecycleOwner) {
            listNFTAdapter.setNFTs(it)
            listNFTAdapter.notifyDataSetChanged()
        }
        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.pbHome.visibility = when (it) {
                true -> View.VISIBLE
                false -> View.GONE
            }
        }

        listNFTAdapter.setOnItemClickCallback(object: OnItemClickCallback {
            override fun onItemClicked(data: NFT) {
                val intent = Intent(context, DetailActivity::class.java)
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