package com.djacoronel.goalmentum.presentation.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.presentation.presenters.GoalPresenter
import com.djacoronel.goalmentum.presentation.ui.listeners.GoalRecyclerClickListener
import kotlinx.android.synthetic.main.goal_item.view.*


/**
 * Created by djacoronel on 10/7/17.
 */
class GoalItemAdapter(
        val mView: GoalPresenter.View
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), GoalRecyclerClickListener {

    val mGoals = mutableListOf(Goal("Input Goal", "Forever"))

    private enum class ViewType {
        NORMAL_CARD, INPUT_CARD
    }

    class NormalViewHolder(itemView: View, private val mListener: GoalRecyclerClickListener) : RecyclerView.ViewHolder(itemView) {

        fun bind(goal: Goal) = with(itemView) {
            goal_card_text.text = goal.description

            itemView.setOnClickListener {
                mListener.onClickViewGoal(adapterPosition)
            }
            itemView.setOnLongClickListener {
                mListener.onLongClickView(adapterPosition)
                true
            }
        }
    }

    class InputViewHolder(itemView: View, private val mListener: GoalRecyclerClickListener) : RecyclerView.ViewHolder(itemView) {
        fun bind(goal: Goal) = with(itemView) {

            itemView.setOnClickListener {
                mListener.onClickAddGoal(adapterPosition)
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
            viewHolder.bind(mGoals[position])
        }
    }

    override fun getItemCount() = mGoals.size


    override fun onClickViewGoal(position: Int) {
        val goal = mGoals[position]
        mView.onClickViewGoal(goal.id)
    }

    override fun onClickAddGoal(position: Int) {
        val goal = mGoals[position]
        mView.onClickAddGoal(goal.id)
    }

    override fun onLongClickView(position: Int) {
        val goal = mGoals[position]
        mView.onLongClickGoal(goal.id)
    }

    fun addNewGoals(goals: List<Goal>) {
        mGoals.clear()
        mGoals.addAll(goals)
        val inputGoalEntry = Goal("Input Goal", "Forever")
        mGoals.add(inputGoalEntry)
        notifyDataSetChanged()
    }
}