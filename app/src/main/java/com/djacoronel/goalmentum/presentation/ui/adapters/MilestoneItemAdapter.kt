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
import com.djacoronel.goalmentum.presentation.presenters.GoalPresenter
import com.djacoronel.goalmentum.presentation.ui.listeners.MilestoneRecyclerClickListener
import kotlinx.android.synthetic.main.milestone_item.view.*

/**
 * Created by djacoronel on 10/9/17.
 */
class MilestoneItemAdapter(
        val mView: GoalPresenter.View
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), MilestoneRecyclerClickListener, AdapterItemSwapper {
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

            val workCount = "${milestone.achievedWorks}/${milestone.totalWorks}"
            work_count.text = workCount

            if (milestone.achieved == true) expanded_achieved_icon.visibility = View.VISIBLE
            else expanded_achieved_icon.visibility = View.GONE

            if (mAdapter.mWorks.isEmpty()) placeholder.visibility = View.VISIBLE
            else placeholder.visibility = View.GONE

            work_recycler.layoutManager = LinearLayoutManager(context)
            work_recycler.adapter = mAdapter

            setOnClickListeners()
        }

        fun setOnClickListeners() = with(itemView) {
            work_count.setOnClickListener { mListener.onClickExpandMilestone(adapterPosition) }
            placeholder.setOnClickListener { mListener.onClickExpandMilestone(adapterPosition) }
            item_menu.setOnClickListener { createAndShowPopupMenu() }
            expanded_milestone_card_text.setOnClickListener { mListener.onClickExpandMilestone(adapterPosition) }
        }

        fun createAndShowPopupMenu() {
            val popup = PopupMenu(itemView.context, itemView.item_menu, Gravity.END)
            popup.menuInflater.inflate(R.menu.menu_view_goal, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.title) {
                    "Edit" -> mListener.onClickEditMilestone(adapterPosition)
                    "Delete" -> mListener.onClickDeleteMilestone(adapterPosition)
                }
                true
            }
            popup.show()
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
        mMilestones.addAll(milestones.filter { it.achieved == false }.sortedBy { it.positionInList })
        mMilestones.addAll(milestones.filter { it.achieved == true })

        for (milestoneId in displayedWorks.keys) {
            mWorkAdapters.put(milestoneId, CollapsedWorkItemAdapter(mView, displayedWorks[milestoneId]!!))
        }
        notifyDataSetChanged()
    }

    fun addMilestone(milestone: Milestone) {
        mMilestones.add(milestone)
        mWorkAdapters.put(milestone.id, CollapsedWorkItemAdapter(mView, listOf()))
        notifyItemInserted(mMilestones.indexOf(milestone))
    }

    fun updateMilestone(milestone: Milestone) {
        val milestoneToBeUpdated = mMilestones.find { it.id == milestone.id }
        milestoneToBeUpdated?.let {
            it.description = milestone.description
            it.achieved = milestone.achieved
            notifyItemChanged(mMilestones.indexOf(it))
        }
    }

    fun deleteMilestone(milestoneId: Long) {
        val milestoneToBeDeleted = mMilestones.find { it.id == milestoneId }
        val index = mMilestones.indexOf(milestoneToBeDeleted)
        mMilestones.removeAt(index)
        notifyItemRemoved(index)
    }

    fun updateWorkAdapter(milestoneId: Long, works: List<Work>) {
        mWorkAdapters[milestoneId] = CollapsedWorkItemAdapter(mView, works)
        val milestoneToUpdate = mMilestones.find { it.id == milestoneId }
        milestoneToUpdate?.let { notifyItemChanged(mMilestones.indexOf(it)) }
    }

    fun updateWork(work: Work) {
        mWorkAdapters[work.assignedMilestone]?.updateWork(work)
    }

    override fun swapItemPositions(fromPosition: Int, toPosition: Int){
        val milestone1 = mMilestones[fromPosition]
        val milestone2 = mMilestones[toPosition]
        val tempPosition = milestone1.positionInList

        milestone1.positionInList = milestone2.positionInList
        milestone2.positionInList = tempPosition
        mView.onSwapMilestonePositions(milestone1, milestone2)
        mMilestones.swap(fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun getTotalItems(): Int{
        return mMilestones.size
    }

    fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
        val tmp = this[index1] // 'this' corresponds to the list
        this[index1] = this[index2]
        this[index2] = tmp
    }
}