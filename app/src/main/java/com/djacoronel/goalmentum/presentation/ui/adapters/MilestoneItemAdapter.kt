package com.djacoronel.goalmentum.presentation.ui.adapters

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.presentation.presenters.ViewGoalPresenter
import com.djacoronel.goalmentum.presentation.ui.listeners.MilestoneRecyclerClickListener
import kotlinx.android.synthetic.main.milestone_item.view.*

/**
 * Created by djacoronel on 10/9/17.
 */
class MilestoneItemAdapter(
        val mView: ViewGoalPresenter.View
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), MilestoneRecyclerClickListener {
    val mMilestones = mutableListOf<Milestone>()
    val mWorkAdapters = hashMapOf<Long, CollapsedWorkItemAdapter>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(parent.inflate(R.layout.milestone_item), this)
    }

    private fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        (viewHolder as ViewHolder).bind(mMilestones[position], mWorkAdapters[mMilestones[position].id]!!)
    }

    override fun getItemCount() = mMilestones.size

    class ViewHolder(itemView: View, private val mListener: MilestoneRecyclerClickListener) : RecyclerView.ViewHolder(itemView) {
        fun bind(milestone: Milestone, mAdapter: CollapsedWorkItemAdapter) = with(itemView) {
            expanded_milestone_card_text.text = milestone.description

            if (milestone.achieved == true)
                itemView.expanded_achieved_icon.visibility = View.VISIBLE
            else
                itemView.expanded_achieved_icon.visibility = View.GONE

            itemView.setOnClickListener {
                mListener.onClickExpandMilestone(adapterPosition)
            }
            itemView.setOnLongClickListener {
                val popup = PopupMenu(context, this, Gravity.END)
                popup.menuInflater.inflate(R.menu.menu_view_goal, popup.menu)
                popup.setOnMenuItemClickListener { item ->
                    when (item.title) {
                        "Edit" -> mListener.onClickEditMilestone(adapterPosition)
                        "Delete" -> mListener.onClickDeleteMilestone(adapterPosition)
                    }
                    true
                }
                popup.show()
                true
            }

            itemView.work_recycler.layoutManager = LinearLayoutManager(context)
            itemView.work_recycler.adapter = mAdapter
        }
    }

    override fun onClickExpandMilestone(position: Int) {
        mView.onExpandMilestone(mMilestones[position].id)
    }

    override fun onClickAddMilestone() {
        mView.onClickAddMilestone()
    }

    override fun onClickEditMilestone(position: Int) {
        mView.onClickEditMilestone(mMilestones[position])
    }

    override fun onClickDeleteMilestone(position: Int) {
        mView.onClickDeleteMilestone(mMilestones[position].id)
    }

    fun showMilestones(milestones: List<Milestone>, displayedWorks: HashMap<Long, List<Work>>) {
        mMilestones.clear()
        mMilestones.addAll(milestones)

        for (milestoneId in displayedWorks.keys) {
            val works = displayedWorks[milestoneId]!!
            if (works.size < 3)
                mWorkAdapters.put(milestoneId, CollapsedWorkItemAdapter(mView, milestoneId, works))
            else
                mWorkAdapters.put(milestoneId, CollapsedWorkItemAdapter(mView, milestoneId, works.subList(0, 3)))

        }
        notifyDataSetChanged()
    }

    fun addMilestone(milestone: Milestone) {
        mMilestones.add(milestone)
        mWorkAdapters.put(milestone.id, CollapsedWorkItemAdapter(mView, milestone.id, listOf()))
        notifyItemInserted(mMilestones.indexOf(milestone))
    }

    fun updateMilestone(milestone: Milestone) {
        val milestoneToBeUpdated = mMilestones.find { it.id == milestone.id }
        milestoneToBeUpdated?.let {
            if(it.description != milestone.description || it.achieved != milestone.achieved){
                it.description = milestone.description
                it.achieved = milestone.achieved
                notifyItemChanged(mMilestones.indexOf(milestoneToBeUpdated))
                checkGoalAchieveStatus()
            }
        }
    }

    fun checkGoalAchieveStatus() {
        val milestonesAchieved = mMilestones.filter { it.achieved == true }
        val milestonesWithoutInputItem = mMilestones.filter { it.description != "Input Milestone" }.size
        val isAllMilestoneAchieved = milestonesAchieved.size == milestonesWithoutInputItem

        if (isAllMilestoneAchieved) {
            mView.onAllMilestonesAchieved()
        }
    }

    fun deleteMilestone(milestoneId: Long) {
        val milestoneToBeDeleted = mMilestones.find { it.id == milestoneId }
        val index = mMilestones.indexOf(milestoneToBeDeleted)
        mMilestones.removeAt(index)
        notifyItemRemoved(index)
    }

    fun updateWork(work: Work) {
        mWorkAdapters[work.assignedMilestone]?.updateWork(work)
    }
}