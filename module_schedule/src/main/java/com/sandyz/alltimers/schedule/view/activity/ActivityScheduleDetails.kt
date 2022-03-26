package com.sandyz.alltimers.schedule.view.activity

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.sandyz.alltimers.common.base.BaseActivity
import com.sandyz.alltimers.common.config.SCHEDULE_DETAILS
import com.sandyz.alltimers.common.config.SCHEDULE_EDIT
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.schedule.R
import com.sandyz.alltimers.common.legacy.ScheduleData
import com.sandyz.alltimers.schedule.model.ScheduleReader
import com.sandyz.alltimers.schedule.view.adapter.ScheduleDetailsPagerAdapter
import kotlinx.android.synthetic.main.schedule_activity_schedule_details.*

@Route(path = SCHEDULE_DETAILS)
class ActivityScheduleDetails : BaseActivity() {
    private var scheduleData: ScheduleData? = null

    private var adapter: ScheduleDetailsPagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.schedule_activity_schedule_details)

        val id = intent?.getIntExtra("schedule_id", -1)
        scheduleData = id?.let { ScheduleReader.db?.scheduleDao()?.findScheduleData(it) }
        if (scheduleData == null) finish()

        schedule_detail_iv_back.setOnClickAction { finish() }

        schedule_detail_iv_edit.setOnClickAction {
            scheduleData?.let { ARouter.getInstance().build(SCHEDULE_EDIT).withInt("schedule_id", it.id).navigation() }
            finish()
        }

        schedule_vp.adapter = scheduleData?.let {
            ScheduleDetailsPagerAdapter(
                it, mutableListOf(
                    R.layout.schedule_layout_details_style_1,
                    R.layout.schedule_layout_details_style_2,
                    R.layout.schedule_layout_details_style_3,
                    R.layout.schedule_layout_details_style_4
                )
            ).also { a ->
                adapter = a
            }
        }

        schedule_tv_page.text = "${(scheduleData?.style ?: 0) + 1}/${adapter?.layoutList?.size ?: 0}"
        schedule_vp.setCurrentItem((scheduleData?.style ?: 0), false)

        schedule_vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {
                schedule_tv_page.text = "${position + 1}/${adapter?.layoutList?.size ?: 0}"
                scheduleData?.style = position
                ScheduleReader.db?.scheduleDao()?.insert(scheduleData)
            }
        })


    }
}