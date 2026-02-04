package com.daedan.festabook.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.createGraph

@DependencyGraph(AppScope::class)
interface IosAppGraph : FestabookAppGraph

fun createIosAppGraph() = createGraph<IosAppGraph>()
