package com.daedan.festabook.presentation.home.model

import com.daedan.festabook.domain.model.LineupItem
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

data class LineUpItemOfDayUiModel(
    val id: Long,
    val date: LocalDate,
    val isDDay: Boolean,
    val lineupItems: List<LineupItemUiModel>,
)

data class LineUpItemGroupUiModel(
    private val group: Map<LocalDate, List<LineupItemUiModel>>,
    private val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
) {
    fun getLineupItems(): List<LineUpItemOfDayUiModel> =
        group.entries.map {
            LineUpItemOfDayUiModel(
                id = it.hashCode().toLong(),
                date = it.key,
                isDDay = it.key == today,
                lineupItems = it.value,
            )
        }
}

fun Map<LocalDate, List<LineupItem>>.toUiModel(): LineUpItemGroupUiModel =
    LineUpItemGroupUiModel(
        group =
            this.mapValues { (_, lineupItems) ->
                lineupItems.map { it.toUiModel() }
            },
    )
