package com.djacoronel.goalmentum.presentation.ui.adapters

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
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
        return when {
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
        }
    }

    class ExpandedViewHolder(itemView: View, private val mListener: MilestoneRecyclerClickListener) : RecyclerView.ViewHolder(itemView) {
        fun bind(milestone: Milestone, mAdapter: WorkItemAdapter?) = with(itemView) {
            expanded_milestone_card_text.text = milestone.description

            itemView.setOnClickListener {
                mListener.onClickCollapseMilestone(adapterPosition)
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
            itemView.work_recycler.isNestedScrollingEnabled = false
            runLayoutAnimation(itemView.work_recycler)
        }

        private fun runLayoutAnimation(recyclerView: RecyclerView){
            val context = recyclerView.context
            val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)

            recyclerView.layoutAnimation = controller
            recyclerView.adapter.notifyDataSetChanged()
            recyclerView.scheduleLayoutAnimation()
        }
    }

    override fun onClickExpandMilestone(position: Int) {
        val id = mMilestones[position].id
        mView.onExpandMilestone(id)

        mWorkAdapters.put(id, WorkItemAdapter(mView, id))
        mExpandedMilestones.add(mMilestones[position])
        notifyItemChanged(position)
    }

    override fun onClickCollapseMilestone(position: Int) {
        mWorkAdapters.remove(mMilestones[position].id)
        mExpandedMilestones.remove(mMilestones[position])
        notifyItemChanged(position)
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

    fun showMilestones(milestones: List<Milestone>) {
        mMilestones.clear()
        mMilestones.addAll(milestones)
        mMilestones.add(Milestone(0, "Input Milestone"))

        onClickExpandMilestone(0)
        notifyDataSetChanged()
    }

    fun addMilestone(milestone: Milestone){
        mMilestones.add(mMilestones.lastIndex,milestone)
        notifyItemInserted(mMilestones.indexOf(milestone))
    }

    fun updateMilestone(milestone: Milestone){
        val milestoneToBeUpdated = mMilestones.find { it.id == milestone.id }
        milestoneToBeUpdated?.description = milestone.description
        notifyItemChanged(mMilestones.indexOf(milestoneToBeUpdated))
    }

    fun deleteMilestone(milestoneId: Long){
        val milestoneToBeDeleted = mMilestones.find { it.id == milestoneId }
        val index = mMilestones.indexOf(milestoneToBeDeleted)
        mMilestones.removeAt(index)
        notifyItemRemoved(index)
    }

}