package com.daedan.festabook.presentation

expect class ContextFactory {
    fun getContext(): Any

    fun getApplication(): Any

    fun getActivity(): Any
}
