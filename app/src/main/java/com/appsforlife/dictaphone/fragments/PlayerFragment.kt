package com.appsforlife.dictaphone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.appsforlife.dictaphone.R
import com.appsforlife.dictaphone.databinding.FragmentPlayerBinding
import com.appsforlife.dictaphone.viewModels.PlayerViewModel
import com.appsforlife.dictaphone.support.Constants
import com.appsforlife.dictaphone.viewModelFactory.PlayerViewModelFactory

class PlayerFragment : DialogFragment() {

    private lateinit var viewModel: PlayerViewModel
    private var recordPath: String? = null
    private lateinit var binding: FragmentPlayerBinding

    fun newInstance(recordPath: String?): PlayerFragment {
        val fragment = PlayerFragment()
        val bundle = Bundle()
        bundle.putString(Constants.ARG_RECORD_PATH, recordPath)
        fragment.arguments = bundle
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_player, container, false)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recordPath = arguments?.getString(Constants.ARG_RECORD_PATH)

        binding.playerView.showTimeoutMs = 0

        val application = requireNotNull(this.activity).application
        val viewModelFactory = recordPath?.let { PlayerViewModelFactory(it, application) }

        viewModel = ViewModelProvider(this, viewModelFactory!!).get(PlayerViewModel::class.java)

        viewModel.itemPath = recordPath
        viewModel.player.observe(viewLifecycleOwner, Observer {
            binding.playerView.player = it
        })


    }

}