package com.daedan.festabook.presentation.news

import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.tab_faq
import festabookkmp.composeapp.generated.resources.tab_lost_item
import festabookkmp.composeapp.generated.resources.tab_notice
import org.jetbrains.compose.resources.StringResource

enum class NewsTab(
    val tabNameRes: StringResource,
) {
    NOTICE(Res.string.tab_notice),
    FAQ(Res.string.tab_faq),
    LOST_ITEM(Res.string.tab_lost_item),
}
