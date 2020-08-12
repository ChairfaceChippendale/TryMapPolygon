package ujujzk.com.trymappolygon

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import kotlin.random.Random

fun getData(): List<FatMarker> {

    val fatsText = List(10) {
        Random.nextDouble(-0.031111, 0.031111) to Random.nextDouble(-0.031111, 0.031111)
    }.map {
        FatMarker(
                position = LatLng(48.4221935 + it.first, 35.1463538 + it.second),

                type = PlaceType.TEXT,
                text = "GG",
                personUrl = ""
        )
    }

    val fatsPerson = List(10) {
        Random.nextDouble(-0.031111, 0.031111) to Random.nextDouble(-0.031111, 0.031111)
    }.map {
        FatMarker(
                position = LatLng(48.4221935 + it.first, 35.1463538 + it.second),

                type = PlaceType.PERSON,
                text = "",
                personUrl = "https://sidekickselling.blob.core.windows.net/profilemedia/57b1514f-5ecf-4eda-b172-a98d1b74a89b.png"
        )
    }

    val fatsTextAvatar = List(10) {
        Random.nextDouble(-0.031111, 0.031111) to Random.nextDouble(-0.031111, 0.031111)
    }.map {
        FatMarker(
                position = LatLng(48.4221935 + it.first, 35.1463538 + it.second),

                type = PlaceType.TEXT,
                text = "AAD",
                personUrl = "",

                btmLeftLabel = FatLabel(
                        imageUrl = "https://sidekickselling.blob.core.windows.net/profilemedia/57b1514f-5ecf-4eda-b172-a98d1b74a89b.png"
                )

        )
    }

    val fatsTextText = List(10) {
        Random.nextDouble(-0.031111, 0.031111) to Random.nextDouble(-0.031111, 0.031111)
    }.map {
        FatMarker(
                position = LatLng(48.4221935 + it.first, 35.1463538 + it.second),

                type = PlaceType.TEXT,
                text = "AAD",
                personUrl = "",

                topRightLabel = FatLabel(
                        text = "13"
                )

        )
    }

    val fatsTextIcon = List(10) {
        Random.nextDouble(-0.031111, 0.031111) to Random.nextDouble(-0.031111, 0.031111)
    }.map {
        FatMarker(
                position = LatLng(48.4221935 + it.first, 35.1463538 + it.second),

                type = PlaceType.BUILDING,
                text = "",
                personUrl = "",
                icon = R.drawable.ic_bike,

                topRightLabel = FatLabel(
                        icon = R.drawable.ic_bathroom_mini,
                        bgColor = R.color.appButterYellow
                )

        )
    }

    val fatsEmptyIcon = List(10) {
        Random.nextDouble(-0.031111, 0.031111) to Random.nextDouble(-0.031111, 0.031111)
    }.map {
        FatMarker(
                position = LatLng(48.4221935 + it.first, 35.1463538 + it.second),

                type = PlaceType.EMPTY,
                text = "",
                personUrl = "",

                topLeftLabel = FatLabel(
                        icon = R.drawable.ic_area_mini,
                        bgColor = R.color.appEnergyF
                ),
                btmLeftLabel = FatLabel(
                        imageUrl = "https://sidekickselling.blob.core.windows.net/profilemedia/57b1514f-5ecf-4eda-b172-a98d1b74a89b.png"
                )
        )
    }

    return fatsText + fatsPerson + fatsTextAvatar + fatsTextText + fatsTextIcon + fatsEmptyIcon

}

data class FatMarker(
        val position: LatLng,

        val type: PlaceType, //central icon and color
        val text: String, //central text
        val personUrl: String, //central
        @DrawableRes val icon: Int = -1,


        val topLeftLabel: FatLabel? = null,
        val topRightLabel: FatLabel? = null,
        val btmLeftLabel: FatLabel? = null,
        val btmRightLabel: FatLabel? = null
)

data class FatLabel(
        @ColorRes val bgColor: Int = R.color.appWhite,
        val imageUrl: String? = null,
        val text: String? = null,
        @DrawableRes val icon: Int = -1
)

enum class PlaceType {
    EMPTY,
    TEXT,
    BUILDING,
    PERSON
}

enum class LabelPosition {
    TOP_LEFT,
    TOP_RIGHT,
    BTM_LEFT,
    BTM_RIGHT
}
