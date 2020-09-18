package com.davenet.gadsleaderboard.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davenet.gadsleaderboard.R
import com.davenet.gadsleaderboard.databinding.FragmentSkillsBinding
import com.davenet.gadsleaderboard.databinding.LearnerSkillsItemBinding
import com.davenet.gadsleaderboard.domain.LeaderSkill
import com.davenet.gadsleaderboard.viewmodels.SkillsViewModel

class FragmentSkills : Fragment() {

    private val viewModel: SkillsViewModel by lazy {
        val activity = requireNotNull(this.activity){}
        ViewModelProvider(this, SkillsViewModel.Factory(activity.application))
            .get(SkillsViewModel::class.java)
    }

    private var viewModelAdapter: SkillsAdapter? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.learnersList.observe(viewLifecycleOwner, {learnerSkills ->
            learnerSkills?.apply {
                viewModelAdapter?.learnerSkills = learnerSkills
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSkillsBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_skills,
            container,
            false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModelAdapter = SkillsAdapter()
        binding.root.findViewById<RecyclerView>(R.id.learner_skills_recycler_view).apply {
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

    class SkillsAdapter: RecyclerView.Adapter<SkillsViewHolder>() {
        var learnerSkills: List<LeaderSkill> = emptyList()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillsViewHolder {
            val withDataBinding: LearnerSkillsItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                SkillsViewHolder.LAYOUT,
                parent,
                false)
            return SkillsViewHolder(withDataBinding)
        }

        override fun onBindViewHolder(holder: SkillsViewHolder, position: Int) {
            holder.viewDataBinding.also {
                it.skill = learnerSkills[position]
            }
        }

        override fun getItemCount() = learnerSkills.size
    }
}

class SkillsViewHolder(val viewDataBinding: LearnerSkillsItemBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.learner_skills_item
    }
}