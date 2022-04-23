package com.djevannn.nftmarketplace.ui.user.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.djevannn.nftmarketplace.R
import com.djevannn.nftmarketplace.ViewModelFactory
import com.djevannn.nftmarketplace.data.User
import com.djevannn.nftmarketplace.databinding.FragmentProfileBinding
import com.djevannn.nftmarketplace.helper.UserPreference
import com.djevannn.nftmarketplace.helper.formatNumber
import com.djevannn.nftmarketplace.setting.SettingActivity
import com.djevannn.nftmarketplace.ui.user.collection.CollectionActivity
import com.djevannn.nftmarketplace.ui.user.edit_profile.EditProfileActivity
import com.djevannn.nftmarketplace.ui.user.favorite.FavoriteActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "settings"
)

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(
                this,
                ViewModelFactory(
                    UserPreference.getInstance(
                        requireContext().dataStore
                    )
                )
            )[ProfileViewModel::class.java]

        _binding = FragmentProfileBinding.inflate(
            inflater,
            container,
            false
        )

        viewModel.getUser().observe(viewLifecycleOwner) {
            val front = it.wallet.take(6)
            val back = it.wallet.takeLast(4)
            val truncate = "$front...$back"

            binding.apply {
                tvWallet.text = it.wallet
                tvName.text = it.name
                tvWallet.text = truncate
                tvEth.text = "${formatNumber(it.balance)} ETH"
                Glide.with(root)
                    .load(it.photo_url)
                    .apply(RequestOptions().override(300, 300))
                    .into(ivAvatar)

                btnTopup.setOnClickListener { _ ->
                    showConfirmDialog(it)
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.pbProfile.visibility = when (it) {
                true -> View.VISIBLE
                false -> View.GONE
            }
        }

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        viewModel.resetMessage()
        viewModel.getLocale.observe(viewLifecycleOwner) {
            binding.tvLanguage.text = it
        }
        viewModel.message.observe(viewLifecycleOwner) {
            if (it != "") Toast.makeText(
                context,
                it,
                Toast.LENGTH_SHORT
            ).show()
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.pbProfile.visibility =
                when (it) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
        }

        binding.btnCollection.setOnClickListener {
            val intent = Intent(
                context,
                CollectionActivity::class.java
            )
            startActivity(intent)
        }
        binding.btnProfile.setOnClickListener {
            val intent = Intent(
                context,
                EditProfileActivity::class.java
            )
            startActivity(intent)
        }
        binding.btnFavorite.setOnClickListener {
            val intent = Intent(
                context,
                FavoriteActivity::class.java
            )
            startActivity(intent)
        }
        binding.btnSetting.setOnClickListener {
            val intent = Intent(
                context,
                SettingActivity::class.java
            )
            startActivity(intent)
        }
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            activity?.finish()
        }
        binding.btnLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showConfirmDialog(data: User) {
        val builder =
            AlertDialog.Builder(requireContext(), 0).create()
        val view =
            layoutInflater.inflate(R.layout.dialog_topup, null)
        val etAmount =
            view.findViewById<TextView>(R.id.et_amount)
        val btnConfirm = view.findViewById<Button>(R.id.btn_confirm)
        val btnCancel = view.findViewById<Button>(R.id.btn_cancel)

        builder.setView(view)

        btnConfirm.setOnClickListener {
            val value = if (etAmount.text.isNotEmpty()) {
                etAmount.text.toString().toDouble()
            } else 0.0

            if (value != 0.0) {
                viewModel.topUp(data, value)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.invalid_amount_of_eth),
                    Toast.LENGTH_SHORT
                ).show()
            }
            builder.dismiss()
        }
        btnCancel.setOnClickListener {
            builder.dismiss()
        }

        builder.show()
    }
}