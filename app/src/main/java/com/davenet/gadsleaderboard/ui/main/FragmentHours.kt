package com.davenet.gadsleaderboard.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davenet.gadsleaderboard.R
import com.davenet.gadsleaderboard.databinding.FragmentHoursBinding
import com.davenet.gadsleaderboard.databinding.LearnerHoursItemBinding
import com.davenet.gadsleaderboard.domain.LeaderHour
import com.davenet.gadsleaderboard.viewmodels.HoursViewModel

class FragmentHours : Fragment() {

    private val viewModel: HoursViewModel by lazy {
        val activity = requireNotNull(this.activity){}
        ViewModelProvider(this, HoursViewModel.Factory(activity.application))
            .get(HoursViewModel::class.java)
    }

    private var viewModelAdapter: HoursAdapter? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.learnersList.observe(viewLifecycleOwner, {learnerHours ->
            learnerHours?.apply {
                viewModelAdapter?.learnerHours = learnerHours
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentHoursBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_hours,
            container,
            false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModelAdapter = HoursAdapter()
        binding.root.findViewById<RecyclerView>(R.id.learner_hours_recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }

        // Observer for the network error
        viewModel.eventNetworkError.observe(viewLifecycleOwner, {isNetworkError ->
            if (isNetworkError) onNetworkError()
        })
        // Inflate the layout for this fragment
        return binding.root
    }

    /**
     * Method for displaying a Toast error message for network errors.
     */
    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

    class HoursAdapter: RecyclerView.Adapter<HoursViewHolder>() {
        var learnerHours: List<LeaderHour> = emptyList()
            set(value) {
            field = value
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoursViewHolder {
            val withDataBinding: LearnerHoursItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                HoursViewHolder.LAYOUT,
                parent,
                false)
            return HoursViewHolder(withDataBinding)
        }

        override fun onBindViewHolder(holder: HoursViewHolder, position: Int) {
            holder.viewDataBinding.also {
                it.hour = learnerHours[position]
            }
        }

        override fun getItemCount() = learnerHours.size
    }
}

class HoursViewHolder(val viewDataBinding: LearnerHoursItemBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.learner_hours_item
    }
}