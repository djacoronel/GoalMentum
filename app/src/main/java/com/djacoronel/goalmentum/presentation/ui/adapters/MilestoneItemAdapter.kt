package com.djacoronel.goalmentum.presentation.ui.adapters

import android.content.Context
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
import kotlinx.android.synthetic.main.collapsed_milestone_item.view.*
import kotlinx.android.synthetic.main.expanded_milestone_item.view.*
import kotlinx.android.synthetic.main.input_recyler_item.view.*

/**
 * Created by djacoronel on 10/9/17.
 */
class MilestoneItemAdapter(
        val mView: ViewGoalPresenter.View
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), MilestoneRecyclerClickListener {
    val mMilestones = mutableListOf<Milestone>()
    val mExpandedMilestones = mutableListOf<Milestone>()
    val mWorkAdapters = hashMapOf<Long, WorkItemAdapter>()

    private enum class ViewType {
        COLLAPSED_CARD, EXPANDED_CARD, INPUT_CARD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.INPUT_CARD.ordinal -> InputViewHolder(parent.inflate(R.layout.input_recyler_item), this)
            ViewType.EXPANDED_CARD.ordinal -> ExpandedViewHolder(parent.inflate(R.layout.expanded_milestone_item), this)
            else -> CollapsedViewHolder(parent.inflate(R.layout.collapsed_milestone_item), this)
        }
    }

    private fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is InputViewHolder -> viewHolder.bind()
            is ExpandedViewHolder -> viewHolder.bind(mMilestones[position], mWorkAdapters[mMilestones[position].id])
            is CollapsedViewHolder -> viewHolder.bind(mMilestones[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when{
            position == mMilestones.lastIndex -> ViewType.INPUT_CARD.ordinal
            mMilestones[position] in mExpandedMilestones -> ViewType.EXPANDED_CARD.ordinal
            else -> ViewType.COLLAPSED_CARD.ordinal
        }
    }

    override fun getItemCount() = mMilestones.size

    class InputViewHolder(itemView: View, private val mListener: MilestoneRecyclerClickListener) : RecyclerView.ViewHolder(itemView) {
        fun bind() = with(itemView) {
            itemView.input_recycler_text.text = "add sub goal"
            itemView.setOnClickListener {
                mListener.onClickAddMilestone()
            }
        }
    }

    class CollapsedViewHolder(itemView: View, private val mListener: MilestoneRecyclerClickListener) : RecyclerView.ViewHolder(itemView) {
        fun bind(milestone: Milestone) = with(itemView) {
            collapsed_milestone_card_text.text = milestone.description

            itemView.setOnClickListener {
                mListener.onClickExpandMilestone(adapterPosition)
            }
            itemView.setOnLongClickListener {
                val popup = PopupMenu(context, this,Gravity.RIGHT)
                popup.menuInflater.inflate(R.menu.menu_view_goal, popup.menu)
                popup.setOnMenuItemClickListener { item ->
                    when(item.title){
                        "Edit" -> mListener.onClickEditMilestone(adapterPosition)
                        "Delete" -> mListener.onClickDeleteMilestone(adapterPosition)
                    }
                    true
                }
                popup.show()
                true
            }
        }
    }

    class ExpandedViewHolder(itemView: View, private val mListener: MilestoneRecyclerClickListener) : RecyclerView.ViewHolder(itemView) {
        fun bind(milestone: Milestone, mAdapter: WorkItemAdapter?) = with(itemView) {
            expanded_milestone_card_text.text = milestone.description

            itemView.setOnClickListener {
                mListener.onClickCollapseMilestone(adapterPosition)
            }
            itemView.setOnLongClickListener {
                val popup = PopupMenu(context, this,Gravity.RIGHT)
                popup.menuInflater.inflate(R.menu.menu_view_goal, popup.menu)
                popup.setOnMenuItemClickListener { item ->
                    when(item.title){
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
            itemView.work_recycler.isNestedScrollingEnabled = false
        }
    }


    fun addNewMilestones(milestones: List<Milestone>) {
        mMilestones.clear()
        mMilestones.addAll(milestones)
        mMilestones.add(Milestone(0,"Input Milestone"))

        onClickExpandMilestone(0)
        notifyDataSetChanged()
    }

    fun showWorksInMilestone(milestoneId: Long, works: List<Work>) {
        mWorkAdapters[milestoneId]!!.showWorks(works)
    }

    override fun onClickAddMilestone() {
        mView.onClickAddMilestone()
    }

    override fun onClickExpandMilestone(position: Int) {
        val id = mMilestones[position].id
        mWorkAdapters.put(id, WorkItemAdapter(mView, id))
        mView.onExpandMilestone(id)

        mExpandedMilestones.add(mMilestones[position])
        notifyItemChanged(position)
    }

    override fun onClickCollapseMilestone(position: Int) {
        mExpandedMilestones.remove(mMilestones[position])
        mWorkAdapters.remove(mMilestones[position].id)
        notifyItemChanged(position)
    }


    override fun onClickEditMilestone(position: Int) {
        mView.onClickDeleteMilestone(mMilestones[position].id)
    }

    override fun onClickDeleteMilestone(position: Int) {
        mView.onClickDeleteMilestone(mMilestones[position].id)
    }
}