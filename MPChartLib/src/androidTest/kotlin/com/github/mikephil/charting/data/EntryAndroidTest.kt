package com.github.mikephil.charting.data

import android.os.Parcel
import android.os.ParcelFormatException
import androidx.test.runner.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EntryAndroidTest {
	@Test
	fun writeToParcel() {
		val entry = Entry(1f, 2f)
		val parcel = Parcel.obtain()

		entry.writeToParcel(parcel, entry.describeContents())
		parcel.setDataPosition(0)

		val restore = Entry.CREATOR.createFromParcel(parcel)
		assertThat(restore.x).isEqualTo(1f)
		assertThat(restore.y).isEqualTo(2f)
		assertThat(restore.data).isNull()
	}

	@Test
	fun writeToParcel_nonParcelableData() {
		val data = LineData()
		val entry = Entry(1f, 2f, data)
		val parcel = Parcel.obtain()

		try {
			entry.writeToParcel(parcel, entry.describeContents())
			fail("Should have failed")
		} catch (exception: ParcelFormatException) {
			assertThat(exception).hasMessageThat()
				.isEqualTo("Cannot parcel an Entry with non-parcelable data")
		}
	}

	@Test
	fun writeToParcel_parcelableData() {
		val data = Entry(3f, 4f)
		val entry = Entry(1f, 2f, data)
		val parcel = Parcel.obtain()

		entry.writeToParcel(parcel, entry.describeContents())
		parcel.setDataPosition(0)

		val restore = Entry.CREATOR.createFromParcel(parcel)
		assertThat(restore.x).isEqualTo(1f)
		assertThat(restore.y).isEqualTo(2f)
		assertThat((restore.data as Entry).equalTo(data)).isTrue()
	}
}
