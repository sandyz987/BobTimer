package com.sandyz.module_community.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.sandyz.alltimers.common.config.COMMUNITY_ENTRY
import com.sandyz.module_community.R

@Route(path = COMMUNITY_ENTRY)
class ActivityCommunity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)
    }
}