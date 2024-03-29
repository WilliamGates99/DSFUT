package com.xeniac.dsfut.ui.fragments

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import com.xeniac.dsfut.R
import com.xeniac.dsfut.databinding.FragmentPickUpBinding
import com.xeniac.dsfut.models.Status
import com.xeniac.dsfut.ui.viewmodels.MainViewModel
import com.xeniac.dsfut.utils.Constants.ERROR_DSFUT_EMPTY
import com.xeniac.dsfut.utils.Constants.ERROR_NETWORK_CONNECTION
import com.xeniac.dsfut.utils.Constants.PREFERENCE_FIFA_VERSION
import com.xeniac.dsfut.utils.Constants.PREFERENCE_INPUTS
import com.xeniac.dsfut.utils.Constants.PREFERENCE_PARTNER_ID
import com.xeniac.dsfut.utils.Constants.PREFERENCE_PLATFORM
import com.xeniac.dsfut.utils.Constants.PREFERENCE_SECRET_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

@AndroidEntryPoint
class PickUpFragment : Fragment(R.layout.fragment_pick_up) {

    private var _binding: FragmentPickUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    private var platform: String = "xb"
    private var doSpam = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPickUpBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        textInputsBackgroundColor()
        platformRadioButton()
        restoreInputFromPrefs()
        saveInputsOnClick()
        pickUpPlayerOnClick()
        cancelPickOnClick()
        puckUpObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun textInputsBackgroundColor() {
        binding.tiEditPartnerId.setOnFocusChangeListener { _, isFocused ->
            if (isFocused) {
                binding.tiLayoutPartnerId.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.background)
            } else {
                binding.tiLayoutPartnerId.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.grayLight)
            }
        }

        binding.tiEditSecretKey.setOnFocusChangeListener { _, isFocused ->
            if (isFocused) {
                binding.tiLayoutSecretKey.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.background)
            } else {
                binding.tiLayoutSecretKey.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.grayLight)
            }
        }

        binding.tiEditFifaVersion.setOnFocusChangeListener { _, isFocused ->
            if (isFocused) {
                binding.tiLayoutFifaVersion.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.background)
            } else {
                binding.tiLayoutFifaVersion.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.grayLight)
            }
        }

        binding.tiEditMinPrice.setOnFocusChangeListener { _, isFocused ->
            if (isFocused) {
                binding.tiLayoutMinPrice.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.background)
            } else {
                binding.tiLayoutMinPrice.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.grayLight)
            }
        }

        binding.tiEditMaxPrice.setOnFocusChangeListener { _, isFocused ->
            if (isFocused) {
                binding.tiLayoutMaxPrice.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.background)
            } else {
                binding.tiLayoutMaxPrice.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.grayLight)
            }
        }

        binding.tiEditTakeAfter.setOnFocusChangeListener { _, isFocused ->
            if (isFocused) {
                binding.tiLayoutTakeAfter.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.background)
            } else {
                binding.tiLayoutTakeAfter.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.grayLight)
            }
        }
    }

    private fun platformRadioButton() =
        binding.radioGroupPlatform.setOnCheckedChangeListener { _, checkedId ->
            platform = when (checkedId) {
                R.id.btn_xbox -> "xb"
                R.id.btn_playstation -> "ps"
                R.id.btn_pc -> "pc"
                else -> "xb"
            }
        }

    private fun restoreInputFromPrefs() = CoroutineScope(Dispatchers.IO).launch {
        val inputsPrefs = requireContext().getSharedPreferences(PREFERENCE_INPUTS, MODE_PRIVATE)
        val partnerId = inputsPrefs.getString(PREFERENCE_PARTNER_ID, null)
        val secretKey = inputsPrefs.getString(PREFERENCE_SECRET_KEY, null)
        val fifaVersion = inputsPrefs.getString(PREFERENCE_FIFA_VERSION, null)
        val radioBtnId = inputsPrefs.getInt(PREFERENCE_PLATFORM, binding.btnXbox.id)

        withContext(Dispatchers.Main) {
            partnerId?.let {
                binding.tiEditPartnerId.setText(it)
            }

            secretKey?.let {
                binding.tiEditSecretKey.setText(secretKey)
            }

            fifaVersion?.let {
                binding.tiEditFifaVersion.setText(fifaVersion)
            }

            binding.radioGroupPlatform.check(radioBtnId)
        }
    }

    private fun saveInputsOnClick() = binding.btnSave.setOnClickListener {
        saveInputs()
    }

    private fun saveInputs() = CoroutineScope(Dispatchers.IO).launch {
        requireContext().getSharedPreferences(PREFERENCE_INPUTS, MODE_PRIVATE).edit().apply {
            putString(PREFERENCE_PARTNER_ID, binding.tiEditPartnerId.text.toString().trim())
            putString(PREFERENCE_SECRET_KEY, binding.tiEditSecretKey.text.toString().trim())
            putString(PREFERENCE_FIFA_VERSION, binding.tiEditFifaVersion.text.toString().trim())
            putInt(PREFERENCE_PLATFORM, binding.radioGroupPlatform.checkedRadioButtonId)
        }.apply()
    }

    private fun pickUpPlayerOnClick() = binding.toolbar.menu[1].setOnMenuItemClickListener {
        getPickUpInput()
        false
    }

    private fun cancelPickOnClick() = binding.toolbar.menu[0].setOnMenuItemClickListener {
        doSpam = false
        viewModel.cancelPick()
        false
    }

    private fun getPickUpInput() {
        val inputMethodManager = requireContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            requireView().applicationWindowToken, 0
        )

        val partnerId = binding.tiEditPartnerId.text.toString().trim()
        val secretKey = binding.tiEditSecretKey.text.toString().trim()
        val fifaVersion = binding.tiEditFifaVersion.text.toString().trim()

        when {
            partnerId.isBlank() -> {
                binding.tiLayoutPartnerId.requestFocus()
                binding.tiLayoutPartnerId.boxStrokeColor =
                    ContextCompat.getColor(requireContext(), R.color.red)
            }
            secretKey.isBlank() -> {
                binding.tiLayoutSecretKey.requestFocus()
                binding.tiLayoutSecretKey.boxStrokeColor =
                    ContextCompat.getColor(requireContext(), R.color.red)
            }
            fifaVersion.isBlank() -> {
                binding.tiLayoutFifaVersion.requestFocus()
                binding.tiLayoutFifaVersion.boxStrokeColor =
                    ContextCompat.getColor(requireContext(), R.color.red)
            }
            else -> {
                val timestamp = getCurrentTime()
                val signature = getMd5Signature(partnerId, secretKey, timestamp)

                val minPriceInput = binding.tiEditMinPrice.text.toString().trim()
                val maxPriceInput = binding.tiEditMaxPrice.text.toString().trim()
                val takeAfterInput = binding.tiEditTakeAfter.text.toString().trim()

                val minPrice = if (minPriceInput.isNotBlank()) minPriceInput.toInt() else null
                val maxPrice = if (maxPriceInput.isNotBlank()) maxPriceInput.toInt() else null
                val takeAfter = if (takeAfterInput.isNotBlank()) takeAfterInput.toInt() else null

                doSpam = true
                val feedUrl = "$fifaVersion/$platform/$partnerId/$timestamp/$signature"
                pickUpPlayer(feedUrl, minPrice, maxPrice, takeAfter)
            }
        }
    }

    private fun pickUpPlayer(
        feedUrl: String,
        minPrice: Int?,
        maxPrice: Int?,
        takeAfter: Int?
    ) = viewModel.pickUpPlayer(feedUrl, minPrice, maxPrice, takeAfter)

    private fun puckUpObserver() =
        viewModel.playerLiveData.observe(viewLifecycleOwner) { responseEvent ->
            responseEvent.getContentIfNotHandled()?.let { response ->
                when (response.status) {
                    Status.LOADING -> showLoadingAnimation()
                    Status.SUCCESS -> {
                        hideLoadingAnimation()
                        response.data?.let {
                            val player = it.player
                            val playerInfo = requireContext().getString(
                                R.string.pick_up_dialog_success_player,
                                player.tradeID,
                                player.startPrice,
                                player.buyNowPrice,
                                player.assetID,
                                player.resourceID,
                                player.name,
                                player.rating,
                                player.position,
                                player.expires,
                                player.transactionID
                            )

                            MaterialAlertDialogBuilder(requireContext()).apply {
                                setTitle(it.message)
                                setMessage(playerInfo)
                                setPositiveButton(requireContext().getString(R.string.pick_up_dialog_positive)) { _, _ -> }
                                show()
                            }
                        }
                    }
                    Status.ERROR -> {
                        response.message?.let {
                            when {
                                it.contains(ERROR_NETWORK_CONNECTION) -> {
                                    hideLoadingAnimation()
                                    Snackbar.make(
                                        binding.root,
                                        requireContext().getString(R.string.network_error_connection),
                                        LENGTH_INDEFINITE
                                    ).apply {
                                        setAction(requireContext().getString(R.string.network_error_retry)) { getPickUpInput() }
                                        show()
                                    }
                                }
                                it.contains(ERROR_DSFUT_EMPTY) -> {
                                    when (doSpam) {
                                        true -> {
                                            Timber.i("spam goes brrrrrrr…")
                                            showCancelButton()
                                            getPickUpInput()
                                        }
                                        else -> {}
                                    }
                                }
                                else -> {
                                    hideLoadingAnimation()
                                    Snackbar.make(requireView(), it, LENGTH_INDEFINITE)
                                        .show()
                                }
                            }
                        }
                    }
                }
            }
        }

    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        return calendar.timeInMillis.toString()
    }

    private fun getMd5Signature(partnerId: String, secretKey: String, timestamp: String): String {
        val input = partnerId + secretKey + timestamp
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(23, '0')
    }

    private fun showLoadingAnimation() {
        binding.toolbar.menu[1].isVisible = false
        binding.tiEditPartnerId.isEnabled = false
        binding.tiEditSecretKey.isEnabled = false
        binding.tiEditFifaVersion.isEnabled = false
        binding.tiEditMinPrice.isEnabled = false
        binding.tiEditMaxPrice.isEnabled = false
        binding.tiEditTakeAfter.isEnabled = false
        binding.cpiPick.visibility = VISIBLE
    }

    private fun hideLoadingAnimation() {
        binding.cpiPick.visibility = GONE
        binding.toolbar.menu[1].isVisible = true
        binding.tiEditPartnerId.isEnabled = true
        binding.tiEditSecretKey.isEnabled = true
        binding.tiEditFifaVersion.isEnabled = true
        binding.tiEditMinPrice.isEnabled = true
        binding.tiEditMaxPrice.isEnabled = true
        binding.tiEditTakeAfter.isEnabled = true
        hideCancelButton()
    }

    private fun showCancelButton() {
        binding.toolbar.menu[0].isVisible = true
    }

    private fun hideCancelButton() {
        binding.toolbar.menu[0].isVisible = false
    }
}