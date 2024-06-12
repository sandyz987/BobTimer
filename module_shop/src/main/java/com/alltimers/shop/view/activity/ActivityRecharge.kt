package com.alltimers.shop.view.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.alltimers.shop.R
import com.alltimers.shop.bean.RechargeValue
import com.alltimers.shop.databinding.ShopActivityRechargeBinding
import com.alltimers.shop.view.adapter.HorizontalStackTransformerWithRotation
import com.alltimers.shop.view.adapter.RechargePagerAdapter
import com.sandyz.alltimers.common.base.BaseActivity
import com.sandyz.alltimers.common.config.SHOP_RECHARGE
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.extensions.toast
import com.sandyz.alltimers.common.widgets.OptionalDialog

@Route(path = SHOP_RECHARGE)
class ActivityRecharge : BaseActivity() {
    private lateinit var binding: ShopActivityRechargeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.shop_activity_recharge)
        binding.shopVp.adapter = RechargePagerAdapter(
            mutableListOf(
                RechargeValue(100, 1f, false),
                RechargeValue(500, 5f, false),
                RechargeValue(1000, 9f, true),
                RechargeValue(2000, 17f, true),
                RechargeValue(3000, 24f, true)
            )
        )
        binding.shopVp.setPageTransformer(false, HorizontalStackTransformerWithRotation())
        binding.shopVp.offscreenPageLimit = 5
        binding.shopTvBuy.setOnClickAction {
            toast("感谢支持，敬请期待~")
        }
        binding.shopIvBack.setOnClickAction {
            OptionalDialog.show(this, "确定要返回吗？", onDeny = {}) {
                finish()
            }
        }
    }
}