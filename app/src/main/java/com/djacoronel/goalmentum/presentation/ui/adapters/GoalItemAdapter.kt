package com.djacoronel.goalmentum.presentation.ui.adapters

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.presentation.presenters.MainPresenter
import com.djacoronel.goalmentum.presentation.ui.listeners.GoalRecyclerClickListener
import kotlinx.android.synthetic.main.goal_item.view.*


/**
 * Created by djacoronel on 10/7/17.
 */
class GoalItemAdapter(val mView: MainPresenter.View)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), GoalRecyclerClickListener {

    val mGoals = mutableListOf<Goal>()

    private enum class ViewType {
        NORMAL_CARD, INPUT_CARD
    }

    class NormalViewHolder(itemView: View, private val mListener: GoalRecyclerClickListener)
        : RecyclerView.ViewHolder(itemView) {

        lateinit var goal: Goal

        fun bind(goal: Goal) = with(itemView) {
            this@NormalViewHolder.goal = goal
            setTexts()
            setClickListeners()
            setProgress()
        }

        fun setTexts() {
            itemView.achieved_goal_card_text.text = goal.description

            val durationText = goal.duration + " " + goal.getStringRemainingDays()
            itemView.achieved_duration_text.text = durationText

            val totalWorks = goal.activeWork + goal.achievedWork
            val achievedCount = goal.achievedWork.toString() + "/" + totalWorks
            itemView.achieved_count.text = achievedCount
        }

        fun setClickListeners() {
            itemView.setOnClickListener {
                mListener.onClickViewGoal(adapterPosition)
            }
            itemView.setOnLongClickListener {
                createAndShowPopupMenu()
                true
            }
        }

        fun createAndShowPopupMenu() {
            val popup = PopupMenu(itemView.context, itemView, Gravity.END)
            popup.menuInflater.inflate(R.menu.menu_view_goal, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.title) {
                    "Edit" -> mListener.onClickEditGoal(adapterPosition)
                    "Delete" -> mListener.onClickDeleteGoal(adapterPosition)
                }
                true
            }
            popup.show()
        }

        fun setProgress() {
            val totalWorks = goal.activeWork + goal.achievedWork
            val progress = if (totalWorks == 0) 0f else (goal.achievedWork / (totalWorks).toFloat()) * 100
            itemView.circular_progress_bar.setProgressWithAnimation(progress)
            itemView.circle_progress.progress = goal.getMomentumWithDeduction()
        }
    }

    class InputViewHolder(itemView: View, private val mListener: GoalRecyclerClickListener) : RecyclerView.ViewHolder(itemView) {
        fun bind() = with(itemView) {
            itemView.setOnClickListener {
                mListener.onClickAddGoal()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mGoals.lastIndex) ViewType.INPUT_CARD.ordinal else ViewType.NORMAL_CARD.ordinal

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ViewType.NORMAL_CARD.ordinal)
            NormalViewHolder(parent.inflate(R.layout.goal_item), this)
        else
            InputViewHolder(parent.inflate(R.layout.input_goal_item), this)
    }

    private fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is NormalViewHolder) {
            viewHolder.bind(mGoals[position])
        } else if (viewHolder is InputViewHolder) {
            viewHolder.bind()
        }
    }

    override fun getItemCount() = mGoals.size

    override fun onClickViewGoal(position: Int) {
        mView.onClickViewGoal(mGoals[position])
    }

    override fun onClickAddGoal() {
        mView.onClickAddGoal()
    }

    override fun onClickEditGoal(position: Int) {
        mView.onClickEditGoal(mGoals[position])
    }

    override fun onClickDeleteGoal(position: Int) {
        mView.onClickDeleteGoal(mGoals[position])
    }

    fun showGoals(goals: List<Goal>) {
        val unAchievedGoals = goals.filter { it.achieved == false }
        val inputGoalEntry = Goal(unAchievedGoals.lastIndex,"Input Goal", "Forever")

        mGoals.clear()
        mGoals.addAll(unAchievedGoals)
        mGoals.add(inputGoalEntry)
        notifyDataSetChanged()
    }

    fun updateGoal(goal: Goal) {
        val goalToBeUpdated = mGoals.find { it.id == goal.id }
        goalToBeUpdated?.description = goal.description
        notifyItemChanged(mGoals.indexOf(goalToBeUpdated))
    }

    fun deleteGoal(goal: Goal) {
        val goalToBeDeleted = mGoals.find { it.id == goal.id }
        val index = mGoals.indexOf(goalToBeDeleted)
        mGoals.removeAt(index)
        notifyItemRemoved(index)
    }
}