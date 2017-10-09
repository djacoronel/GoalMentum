package com.djacoronel.goalmentum.presentation.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.domain.executor.impl.ThreadExecutor
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.presentation.presenters.GoalPresenter
import com.djacoronel.goalmentum.presentation.presenters.impl.GoalPresenterImpl
import com.djacoronel.goalmentum.presentation.ui.activities.AddGoalActivity
import com.djacoronel.goalmentum.presentation.ui.activities.ViewGoalActivity
import com.djacoronel.goalmentum.presentation.ui.adapters.GoalItemAdapter
import com.djacoronel.goalmentum.storage.GoalRepositoryImpl
import com.djacoronel.goalmentum.threading.MainThreadImpl
import kotlinx.android.synthetic.main.fragment_active_goals.view.*
import org.jetbrains.anko.support.v4.alert


/**
 * Created by djacoronel on 10/7/17.
 */
class ActiveGoalsFragment : Fragment(), GoalPresenter.View {

    private lateinit var mGoalPresenter: GoalPresenter
    private lateinit var mAdapter: GoalItemAdapter

    fun newInstance(): ActiveGoalsFragment {
        return ActiveGoalsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_active_goals, container, false)
        init(view)

        return view
    }

    private fun init(view: View) {
        // setup recycler view adapter
        mAdapter = GoalItemAdapter(this)

        // setup recycler view
        view.goal_recycler.layoutManager = LinearLayoutManager(activity)
        view.goal_recycler.layoutManager.isAutoMeasureEnabled = false
        view.goal_recycler.adapter = mAdapter

        // instantiate the presenter
        mGoalPresenter = GoalPresenterImpl(
                ThreadExecutor.instance,
                MainThreadImpl.instance,
                this,
                GoalRepositoryImpl()
        )

    }

    override fun onClickViewGoal(goal: Goal) {
        val intent = Intent(context, ViewGoalActivity::class.java)
        intent.putExtra(EXTRA_GOAL_ID, goal.id)
        intent.putExtra(EXTRA_GOAL_DESC, goal.description)
        startActivity(intent)
    }

    override fun onClickAddGoal(goalId: Long) {
        val intent = Intent(context, AddGoalActivity::class.java)
        startActivity(intent)
    }

    override fun onLongClickGoal(goalId: Long) {
        alert {
            title = "Edit or delete goal?"
            positiveButton("Edit") { editGoal(goalId) }
            negativeButton("Delete") { mGoalPresenter.deleteGoal(goalId) }
        }.show()
    }

    override fun onGoalDeleted(goal: Goal) {
        mGoalPresenter.getAllGoals()
    }

    fun editGoal(goalId: Long) {
        val intent = Intent(context, AddGoalActivity::class.java)
        intent.putExtra(EXTRA_GOAL_ID, goalId)

        startActivityForResult(intent, EDIT_GOAL_REQUEST)
    }

    companion object {
        const val EXTRA_GOAL_ID = "extra_goal_id_key"
        const val EXTRA_GOAL_DESC = "extra_goal_desc_key"
        const val EDIT_GOAL_REQUEST = 0
    }

    override fun showGoals(goals: List<Goal>) {
        mAdapter.addNewGoals(goals)
    }

    override fun onResume() {
        super.onResume()
        mGoalPresenter.resume()
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError(message: String) {
    }
}