package com.djacoronel.goalmentum.presentation.ui.activities

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.domain.executor.impl.ThreadExecutor
import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.presentation.presenters.AddWorkPresenter
import com.djacoronel.goalmentum.presentation.presenters.impl.AddWorkPresenterImpl
import com.djacoronel.goalmentum.presentation.ui.adapters.ExpandedWorkItemAdapter
import com.djacoronel.goalmentum.storage.GoalRepositoryImpl
import com.djacoronel.goalmentum.storage.MilestoneRepositoryImpl
import com.djacoronel.goalmentum.storage.WorkRepositoryImpl
import com.djacoronel.goalmentum.threading.MainThreadImpl
import kotlinx.android.synthetic.main.activity_milestone.*
import kotlinx.android.synthetic.main.input_dialog.view.*
import org.jetbrains.anko.alert


class MilestoneActivity : AppCompatActivity(), AddWorkPresenter.View {

    lateinit var mAdapter: ExpandedWorkItemAdapter
    lateinit var mAddWorkPresenter: AddWorkPresenter
    var milestoneId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_milestone)

        init()
    }

    private fun init(){
        milestoneId = intent.getLongExtra("extra_milestone_id_key", -1)

        mAddWorkPresenter = AddWorkPresenterImpl(
                ThreadExecutor.instance,
                MainThreadImpl.instance,
                this,
                GoalRepositoryImpl(),
                MilestoneRepositoryImpl(),
                WorkRepositoryImpl()
        )

        add_item_button.setOnClickListener { onClickAddWork(input_item_text.text.toString()) }
        expanded_milestone_card_text.setOnClickListener { finish() }
        expand_button.setOnClickListener { finish() }

        mAdapter = ExpandedWorkItemAdapter(this, milestoneId)
        work_recycler.layoutManager = LinearLayoutManager(this)
        work_recycler.adapter = mAdapter

        mAddWorkPresenter.getMilestoneById(milestoneId)
        mAddWorkPresenter.getAllWorkByAssignedMilestone(milestoneId)
    }

    fun showKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInputFromWindow(view.windowToken, 0, 0)
    }

    fun createInputDialogView(): View {
        val view = View.inflate(this, R.layout.input_dialog, null)
        view.input_item_text.requestFocus()
        view.input_item_text.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE)
                view.add_item_button.performClick()
            false
        }

        showKeyboard()
        return view
    }

    override fun onMilestoneRetrieved(milestone: Milestone) {
        expanded_milestone_card_text.text = milestone.description
        if (milestone.achieved == true) expanded_achieved_icon.visibility = View.VISIBLE
        else expanded_achieved_icon.visibility = View.GONE
    }

    override fun showWorks(milestoneId: Long, works: List<Work>) {
        mAdapter.showWorks(works)
        if(works.isEmpty())
            input_item_text.requestFocus()
    }

    override fun onClickAddWork(workDescription: String) {
        mAddWorkPresenter.addNewWork(milestoneId, workDescription)
    }

    override fun onWorkAdded(work: Work) {
        mAdapter.addWork(work)
    }

    override fun onClickEditWork(work: Work) {
        val view = createInputDialogView()
        val alert = alert { customView = view }.show()

        view.input_item_text.hint = "Work Description"
        view.input_item_text.setText(work.description)

        view.add_item_button.setOnClickListener {
            work.description = view.input_item_text.text.toString()
            mAddWorkPresenter.updateWork(work)
            hideKeyboard(view)
            alert.dismiss()
        }
    }

    override fun onWorkUpdated(work: Work) {
        mAdapter.updateWork(work)
    }

    override fun onClickDeleteWork(workId: Long) {
        mAddWorkPresenter.deleteWork(workId)
    }

    override fun onWorkDeleted(workId: Long) {
        mAdapter.deleteWork(workId)
    }

    override fun onClickToggleWork(work: Work) {
        mAddWorkPresenter.toggleWork(work)
    }

    override fun onWorkToggled(work: Work) {
        mAdapter.updateWork(work)
        mAddWorkPresenter.getMilestoneById(milestoneId)
    }
}