package com.daedan.festabook.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph

@DependencyGraph(AppScope::class)
interface IosAppGraph : FestabookAppGraph
