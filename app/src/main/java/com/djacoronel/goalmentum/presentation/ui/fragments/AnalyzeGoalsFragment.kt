package com.djacoronel.goalmentum.presentation.ui.fragments

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import com.db.chart.animation.Animation
import com.db.chart.model.BarSet
import com.db.chart.model.LineSet
import com.db.chart.renderer.AxisRenderer
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.presentation.presenters.AnalyzeGoalsPresenter
import com.djacoronel.goalmentum.util.DateUtils
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.card_analysis.*
import kotlinx.android.synthetic.main.card_bar_graph.view.*
import kotlinx.android.synthetic.main.card_line_graph.view.*
import kotlinx.android.synthetic.main.fragment_analyze_goals.view.*
import java.util.*
import javax.inject.Inject


class AnalyzeGoalsFragment : Fragment(), AnalyzeGoalsPresenter.View {

    @Inject
    lateinit var analyzeGoalsPresenter: AnalyzeGoalsPresenter

    var colorPrimary: Int = 0
    var colorPrimaryDark: Int = 0
    var colorSecondary: Int = 0
    var colorSecondaryLight: Int = 0
    var colorAccent: Int = 0

    fun newInstance(): AnalyzeGoalsFragment {
        return AnalyzeGoalsFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_analyze_goals, container, false)

        initializeColors()

        analyzeGoalsPresenter.getLineGraphData()
        analyzeGoalsPresenter.getBarGraphData()
        analyzeGoalsPresenter.getAnalysisData()

        runLayoutAnimation(view.analyze_goals)

        return view
    }

    private fun initializeColors() {
        colorPrimary = ContextCompat.getColor(context, R.color.colorPrimary)
        colorPrimaryDark = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        colorSecondary = ContextCompat.getColor(context, R.color.colorSecondary)
        colorSecondaryLight = ContextCompat.getColor(context, R.color.colorSecondaryLight)
        colorAccent = ContextCompat.getColor(context, R.color.colorAccent)
    }

    private fun runLayoutAnimation(layout: LinearLayout) {
        val slideUp = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom)
        layout.layoutAnimation = slideUp
    }

    override fun onWeeklyLineGraphRetrieved(
            currentWeekData: List<Pair<String, Int>>, previousWeekData: List<Pair<String, Int>>) {
        setupLineGraph(currentWeekData, previousWeekData)
    }

    private fun setupLineGraph(
            currentWeekData: List<Pair<String, Int>>, previousWeekData: List<Pair<String, Int>>) {
        val currentWeekSet = createLineSet(currentWeekData)
        val lastWeekSet = createLineSet(previousWeekData)

        val fontSize = dpToPx(12)
        val dotRadius = dpToPx(5).toFloat()
        val dashDimen = dpToPx(5).toFloat()

        with(currentWeekSet) {
            color = colorAccent
            setDotsRadius(dotRadius)
            setDotsColor(colorAccent)
            setFill(Color.LTGRAY)
            endAt(getIndexInGraphOfCurrentDay())
        }

        with(lastWeekSet) {
            color = Color.LTGRAY
            setDotsRadius(0f)
            setDotsColor(Color.LTGRAY)
            setDashed(floatArrayOf(dashDimen, dashDimen))
        }

        view?.let {
            with(it.line_chart) {
                addData(lastWeekSet)
                addData(currentWeekSet)
                setFontSize(fontSize)
                setXAxis(false)
                setYAxis(false)
                setLabelsColor(colorSecondaryLight)
                setStep(1)
                show(Animation(400))
            }
        }
    }

    private fun createLineSet(pointData: List<Pair<String, Int>>): LineSet {
        val lineSet = LineSet()
        pointData.forEach { lineSet.addPoint(it.first, it.second.toFloat()) }

        val lineThickness = dpToPx(6).toFloat()
        lineSet.thickness = lineThickness

        return lineSet
    }

    private fun getIndexInGraphOfCurrentDay(): Int {
        val today = Calendar.getInstance()
        val todayValue = today.get(Calendar.DAY_OF_WEEK)

        return DateUtils.convertValueToMondayFirst(todayValue)
    }

    override fun onWeeklyBarGraphRetrieved(dataBars: List<Pair<String, Int>>) {
        val barSet = BarSet()
        dataBars.forEach { barSet.addBar(it.first, it.second.toFloat()) }
        barSet.color = colorAccent

        val fontSize = dpToPx(12)
        val spacing = dpToPx(4)

        view?.let {
            with(it.bar_chart) {
                addData(barSet)
                setFontSize(fontSize)
                setBarSpacing(spacing.toFloat())
                setXAxis(false)
                setAxisColor(colorSecondaryLight)
                setLabelsColor(colorSecondaryLight)
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
                most_productive_day.text = DateUtils.getDayOfWeekLabel(data[5])
            }
        }
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
}
