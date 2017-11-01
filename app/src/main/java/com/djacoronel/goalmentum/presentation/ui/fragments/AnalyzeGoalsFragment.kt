package com.djacoronel.goalmentum.presentation.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.db.chart.animation.Animation
import com.db.chart.model.Bar
import com.db.chart.model.BarSet
import com.db.chart.model.LineSet
import com.db.chart.model.Point
import com.db.chart.renderer.AxisRenderer

import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.domain.executor.impl.ThreadExecutor
import com.djacoronel.goalmentum.presentation.presenters.AnalyzeGoalsPresenter
import com.djacoronel.goalmentum.presentation.presenters.impl.AnalyzeGoalPresenterImpl
import com.djacoronel.goalmentum.storage.GoalRepositoryImpl
import com.djacoronel.goalmentum.storage.MilestoneRepositoryImpl
import com.djacoronel.goalmentum.storage.WorkRepositoryImpl
import com.djacoronel.goalmentum.threading.MainThreadImpl
import kotlinx.android.synthetic.main.card_analysis.*
import kotlinx.android.synthetic.main.card_bar_graph.view.*
import kotlinx.android.synthetic.main.card_line_graph.view.*
import java.util.*
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_analyze_goals.*
import kotlinx.android.synthetic.main.fragment_analyze_goals.view.*


class AnalyzeGoalsFragment : Fragment(), AnalyzeGoalsPresenter.View {
    var colorPrimary: Int = 0
    var colorPrimaryDark: Int = 0
    var colorAccent: Int = 0

    fun newInstance(): AnalyzeGoalsFragment {
        return AnalyzeGoalsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_analyze_goals, container, false)

        colorPrimary = ContextCompat.getColor(context, R.color.colorPrimary)
        colorPrimaryDark = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        colorAccent = ContextCompat.getColor(context, R.color.colorAccent)

        val analyzeGoalsPresenter = AnalyzeGoalPresenterImpl(
                ThreadExecutor.instance,
                MainThreadImpl.instance,
                this,
                GoalRepositoryImpl(),
                MilestoneRepositoryImpl(),
                WorkRepositoryImpl()
        )

        analyzeGoalsPresenter.getWeeklyLineGraph()
        analyzeGoalsPresenter.getWeeklyBarGraph()
        analyzeGoalsPresenter.getAnalysis()

        runLayoutAnimation(view.analyze_goals)

        return view
    }

    fun runLayoutAnimation(layout: LinearLayout){
        val slideUp = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom)
        layout.layoutAnimation = slideUp
    }

    override fun onWeeklyLineGraphRetrieved(currentWeekData: List<Point>, previousWeekData: List<Point>) {
        setupLineGraph(currentWeekData, previousWeekData)
    }

    private fun setupLineGraph(currentWeekData: List<Point>, previousWeekData: List<Point>){
        val currentWeekSet = createLineSet(currentWeekData)
        val lastWeekSet = createLineSet(previousWeekData)

        with(currentWeekSet) {
            color = colorAccent
            setDotsRadius(10f)
            setDotsColor(colorAccent)

            endAt(getIndexInGraphOfCurrentDay())
        }

        with(lastWeekSet) {
            color = Color.LTGRAY
            setDotsRadius(0f)
            setDotsColor(Color.LTGRAY)

            setDashed(floatArrayOf(10f, 10f))
        }

        view?.let {
            with(it.line_chart) {
                addData(lastWeekSet)
                addData(currentWeekSet)
                setLabelsColor(Color.WHITE)
                setFontSize(30)
                setYLabels(AxisRenderer.LabelPosition.INSIDE)
                show(Animation(400))
            }
        }
    }

    private fun createLineSet(pointData: List<Point>): LineSet{
        val lineSet = LineSet()
        pointData.forEach { lineSet.addPoint(it) }
        return lineSet
    }

    private fun getIndexInGraphOfCurrentDay(): Int{
        val today = Calendar.getInstance()
        val todayNum = today.get(Calendar.DAY_OF_WEEK)
        val graphIndex = arrayOf(6,0,1,2,3,4,5)

        return graphIndex[todayNum]
    }

    override fun onWeeklyBarGraphRetrieved(dataBars: List<Bar>) {
        val barSet = BarSet()
        dataBars.forEach { barSet.addBar(it) }
        barSet.color = colorAccent

        view?.let {
            with(it.bar_chart) {
                addData(barSet)
                setLabelsColor(Color.WHITE)
                setFontSize(30)
                setBarSpacing(8f)
                setYLabels(AxisRenderer.LabelPosition.INSIDE)
                show(Animation(400))
            }
        }
    }

    override fun onAnalysisRetrieved(data: List<Int>) {
        view?.let {
            with(view) {
                average_done_per_day.text = data[0].toString()
                average_done_per_week.text = data[1].toString()
                total_work_done.text = data[2].toString()
                total_milestone_achieved.text = data[3].toString()
                total_goals_achieved.text = data[4].toString()
                val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
                most_productive_day.text = days[data[5]]
            }
        }
    }
}
