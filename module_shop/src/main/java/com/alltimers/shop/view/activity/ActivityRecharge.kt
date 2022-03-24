package com.alltimers.shop.view.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alltimers.shop.R
import com.alltimers.shop.bean.RechargeValue
import com.alltimers.shop.view.adapter.HorizontalStackTransformerWithRotation
import com.alltimers.shop.view.adapter.RechargePagerAdapter
import com.sandyz.alltimers.common.base.BaseActivity
import com.sandyz.alltimers.common.config.SHOP_RECHARGE
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.extensions.toast
import com.sandyz.alltimers.common.widgets.OptionalDialog
import kotlinx.android.synthetic.main.shop_activity_recharge.*

@Route(path = SHOP_RECHARGE)
class ActivityRecharge : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shop_activity_recharge)
        shop_vp.adapter = RechargePagerAdapter(
            mutableListOf(
                RechargeValue(100, 1f, false),
                RechargeValue(500, 5f, false),
                RechargeValue(1000, 9f, true),
                RechargeValue(2000, 17f, true),
                RechargeValue(3000, 24f, true)
            )
        )
        shop_vp.setPageTransformer(false, HorizontalStackTransformerWithRotation())
        shop_vp.offscreenPageLimit = 5
        shop_tv_buy.setOnClickAction {
            toast("感谢支持，敬请期待~")
        }
        shop_iv_back.setOnClickAction {
            OptionalDialog.show(this, "确定要返回吗？", onDeny = {}) {
                finish()
            }
        }
    }
}