package com.daedan.festabook.di

import com.daedan.festabook.presentation.ContextFactory
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph

@DependencyGraph(AppScope::class)
interface IosAppGraph : FestabookAppGraph {
    val contextFactory: ContextFactory
}
