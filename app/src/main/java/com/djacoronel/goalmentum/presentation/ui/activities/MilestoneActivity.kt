package com.djacoronel.goalmentum.presentation.ui.activities

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.presentation.presenters.MilestonePresenter
import com.djacoronel.goalmentum.presentation.ui.adapters.ExpandedWorkItemAdapter
import com.djacoronel.goalmentum.util.TouchHelper
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_milestone.*
import kotlinx.android.synthetic.main.input_dialog.view.*
import org.jetbrains.anko.alert
import javax.inject.Inject


class MilestoneActivity : AppCompatActivity(), MilestonePresenter.View, ExpandedWorkItemAdapter.OnDragStartListener {

    @Inject lateinit var mMilestonePresenter: MilestonePresenter

    lateinit var mAdapter: ExpandedWorkItemAdapter
    lateinit var itemTouchHelper: ItemTouchHelper
    var milestoneId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_milestone)
        init()
    }

    private fun init() {
        milestoneId = intent.getLongExtra("extra_milestone_id_key", -1)

        mMilestonePresenter.getMilestoneById(milestoneId)
        mMilestonePresenter.getAllWorkByAssignedMilestone(milestoneId)

        setOnKeyListeners()
        setupWorkRecycler()
    }

    fun setupWorkRecycler() {
        mAdapter = ExpandedWorkItemAdapter(this, this, milestoneId)
        work_recycler.layoutManager = LinearLayoutManager(this)
        work_recycler.adapter = mAdapter

        val callback = TouchHelper(mAdapter)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(work_recycler)
    }

    fun setOnKeyListeners() {
        fab.setOnClickListener { fabOnClick() }
        add_task_button.setOnClickListener { onClickAddWork() }
        expanded_milestone_card_text.setOnClickListener { finish() }
        work_count.setOnClickListener { finish() }
        item_menu.setOnClickListener { finish() }
        input_work_edittext.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                onClickAddWork()
                return@OnKeyListener true
            }
            false
        })
    }

    fun fabOnClick() {
        showKeyboard()
        input_work_card.visibility = View.VISIBLE
        input_work_card.requestFocus()

        fab.hide()
        fab.visibility = View.GONE
    }

    fun dismissInput() {
        hideKeyboard(input_work_edittext)
        input_work_card.visibility = View.GONE

        fab.show()
        fab.visibility = View.VISIBLE
    }

    fun showKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }

    fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun createInputDialogView(): View {
        val view = View.inflate(this, R.layout.input_dialog, null)
        view.input_work_edittext.requestFocus()
        view.input_work_edittext.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE)
                view.add_task_button.performClick()
            false
        }

        showKeyboard()
        return view
    }

    override fun onMilestoneRetrieved(milestone: Milestone) {
        expanded_milestone_card_text.text = milestone.description

        val workCount = "${milestone.achievedWorks}/${milestone.totalWorks}"
        work_count.text = workCount

        if (milestone.achieved == true) expanded_achieved_icon.visibility = View.VISIBLE
        else expanded_achieved_icon.visibility = View.GONE
    }

    override fun showWorks(milestoneId: Long, works: List<Work>) {
        mAdapter.showWorks(works)
        if (works.isEmpty()) fab.performClick()
    }

    override fun onClickAddWork() {
        val workDescription = input_work_edittext.text.toString()
        mMilestonePresenter.addNewWork(milestoneId, workDescription)
    }

    override fun onWorkAdded(work: Work) {
        mMilestonePresenter.getMilestoneById(milestoneId)
        mAdapter.addWork(work)
        work_recycler.smoothScrollToPosition(mAdapter.itemCount)
        input_work_edittext.text.clear()
    }

    override fun onClickEditWork(work: Work) {
        val view = createInputDialogView()
        val alert = alert { customView = view }.show()

        view.input_work_edittext.hint = "Work Description"
        view.input_work_edittext.setText(work.description)

        view.add_task_button.setOnClickListener {
            work.description = view.input_work_edittext.text.toString()
            mMilestonePresenter.updateWork(work)
            hideKeyboard(view)
            alert.dismiss()
        }
    }

    override fun onWorkUpdated(work: Work) {
        mAdapter.updateWork(work)
    }

    override fun onClickDeleteWork(workId: Long) {
        mMilestonePresenter.deleteWork(workId)
    }

    override fun onWorkDeleted(workId: Long) {
        mAdapter.deleteWork(workId)
        mMilestonePresenter.getMilestoneById(milestoneId)
    }

    override fun onClickToggleWork(work: Work) {
        mMilestonePresenter.toggleWork(work)
    }

    override fun onWorkToggled(work: Work) {
        mAdapter.updateWork(work)
        mMilestonePresenter.getMilestoneById(milestoneId)
    }

    override fun onSwapWorkPositions(work1: Work, work2: Work) {
        mMilestonePresenter.swapWorkPositions(work1, work2)
    }

    override fun onDragStarted(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }
}