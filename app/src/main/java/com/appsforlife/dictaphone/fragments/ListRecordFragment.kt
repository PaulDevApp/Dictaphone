package com.appsforlife.dictaphone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.appsforlife.dictaphone.R
import com.appsforlife.dictaphone.adapters.RecordAdapter
import com.appsforlife.dictaphone.database.RecordDB
import com.appsforlife.dictaphone.databinding.FragmentListRecordBinding
import com.appsforlife.dictaphone.viewModels.ListViewModel
import com.appsforlife.dictaphone.viewModels.ListViewModelFactory

class ListRecordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentListRecordBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list_record, container, false
        )

        val application = requireNotNull(this.activity).application

        val dataSource = RecordDB.getInstance(application).recordDAO
        val viewModelFactory = ListViewModelFactory(dataSource)

        val listRecordViewModel =
            ViewModelProvider(this, viewModelFactory).get(ListViewModel::class.java)

        binding.listRecordViewModel = listRecordViewModel

        val adapter =
            RecordAdapter()
        binding.rvList.adapter = adapter

        listRecordViewModel.records.observe(viewLifecycleOwner, {
            it?.let {
                adapter.data = it
            }
        })

        binding.lifecycleOwner = this


        return binding.root
    }

}