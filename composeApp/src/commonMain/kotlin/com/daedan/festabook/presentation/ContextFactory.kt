package com.daedan.festabook.presentation

expect class ContextFactory {
    fun createActivityContext(): Any

    fun createApplicationContext(): Any

    fun createActivity(): Any
}
