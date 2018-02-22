package com.github.mikephil.charting.utils

import com.github.mikephil.charting.data.Entry
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class EntryXComparatorTest {
	@Test
	fun compare() {
		val entry1 = Entry(1f, 1f)
		val entry2 = Entry(2f, 2f)
		val entry3 = Entry(3f, 3f)
		val entry4 = Entry(4f, 4f)
		val entries = arrayOf(entry3, entry1, entry4, entry2)
		entries.sortWith(EntryXComparator())

		assertThat(entries).isEqualTo(arrayOf(entry1, entry2, entry3, entry4))
	}
}
