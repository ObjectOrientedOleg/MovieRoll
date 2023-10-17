package com.objectorientedoleg.common.immutable

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

inline fun <T> buildPersistentList(block: PersistentList.Builder<T>.() -> Unit): PersistentList<T> =
    persistentListOf<T>().builder().apply(block).build()

inline fun <T, R> Iterable<T>.mapToImmutableList(transform: (T) -> R): ImmutableList<R> =
    buildPersistentList {
        this@mapToImmutableList.forEach { add(transform(it)) }
    }

inline fun <T, R : Any> Iterable<T>.mapNotNullToImmutableList(transform: (T) -> R?): ImmutableList<R> =
    buildPersistentList {
        this@mapNotNullToImmutableList.forEach { t ->
            transform(t)?.let { r -> add(r) }
        }
    }