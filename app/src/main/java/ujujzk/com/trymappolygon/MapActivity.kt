package ujujzk.com.trymappolygon

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.widget.ImageViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.coroutineScope
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.bumptech.glide.Glide
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ui.IconGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ujujzk.com.trymappolygon.databinding.ActivityMapBinding
import kotlin.random.Random

//https://www.youtube.com/watch?v=lchyOhPREh4

class MapActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapBinding

    var selectedMarker: Marker? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (googleServicesAvailable()) {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_map)
            initMap()
        } else {
            //if google googleMap is not supported there must be alternative layout
        }
    }

    private fun initMap() {

        val mapFragment = supportFragmentManager.findFragmentById(R.id.fragment_map) as? SupportMapFragment


        lifecycle.coroutineScope.launchWhenCreated {

            val googleMap = mapFragment?.awaitMap()


            val pos = LatLng(48.4221935, 35.1463538)


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
                        personUrl = "http://waleedsarwar.com/assets/img/me.png"
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
                                imageUrl = "http://waleedsarwar.com/assets/img/me.png"
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
                                bgColor = ContextCompat.getColor(this@MapActivity, R.color.appButterYellow)
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
                                bgColor = ContextCompat.getColor(this@MapActivity, R.color.appEnergyF)
                        ),
                        btmLeftLabel = FatLabel(
                                imageUrl = "http://waleedsarwar.com/assets/img/me.png"
                        )
                )
            }






            (fatsText + fatsPerson + fatsTextAvatar + fatsTextText + fatsTextIcon + fatsEmptyIcon).forEach {
                addCustomMarkerFromURL(googleMap, it, isSelected = false)
            }


            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15F))

            googleMap?.setOnMarkerClickListener {

                lifecycle.coroutineScope.launchWhenCreated {
                    selectedMarker?.setIcon(makeMarkerIcon(selectedMarker?.tag as FatMarker, false))
                }

                if (selectedMarker == it) {
                    selectedMarker = null
                } else {

                    lifecycle.coroutineScope.launchWhenCreated {
                        it.setIcon(makeMarkerIcon(it.tag as FatMarker, true))
                    }

                    selectedMarker = it
                }
                true
            }

        }
    }

    private suspend fun addCustomMarkerFromURL(map: GoogleMap?, fat: FatMarker, isSelected: Boolean) {

        map?.addMarker(
                MarkerOptions()
                        .position(fat.position)
                        .icon(makeMarkerIcon(fat, isSelected))
        )?.apply {
            tag = fat
        }

    }


    private suspend fun makeMarkerIcon(fat: FatMarker, isSelected: Boolean): BitmapDescriptor {

        val person = loadImage(fat.personUrl)

        return BitmapDescriptorFactory.fromBitmap(
                makeMarkerView(
                        person,
                        fat,
                        isSelected = isSelected
                ).makeIcon(fat.text)
        )

    }

    private suspend fun loadImage(imageUrl: String?) = withContext(Dispatchers.IO) {

        if (imageUrl.isNullOrBlank()) return@withContext null

        val bm = kotlin.runCatching {
            Glide.with(this@MapActivity)
                    .asBitmap()
                    .load(imageUrl)
                    .fitCenter()
                    .submit()
                    .get()
        }
                .onFailure { it.printStackTrace() }
                .getOrNull()
                ?: return@withContext null

        RoundedBitmapDrawableFactory.create(
                resources,
                Bitmap.createScaledBitmap(bm, 50, 50, false)
        ).apply {
            isCircular = true
        }
    }


    private suspend fun makeMarkerView(person: Drawable?, fat: FatMarker, isSelected: Boolean): IconGenerator {

        val view: ViewGroup = this.inflate(
                if (isSelected) R.layout.view_map_fat_marker_selected
                else R.layout.view_map_fat_marker
        ) as ViewGroup
        if (person != null) {
            view.findViewById<ImageView>(R.id.person).setImageDrawable(person)
        } else if (fat.icon > 0) {
            view.findViewById<ImageView>(R.id.person).setPadding(20, 20, 20, 20)
            view.findViewById<ImageView>(R.id.person).setImageResource(fat.icon)
        } else {
            view.findViewById<ImageView>(R.id.person).gone()
        }

        fat.topLeftLabel?.let {
            view.addView(makeLabel(it, place = LabelPosition.TOP_LEFT, isSelected = isSelected))
        }
        fat.btmLeftLabel?.let {
            view.addView(makeLabel(it, place = LabelPosition.BTM_LEFT, isSelected = isSelected))
        }
        fat.topRightLabel?.let {
            view.addView(makeLabel(it, place = LabelPosition.TOP_RIGHT, isSelected = isSelected))
        }
        fat.btmRightLabel?.let {
            view.addView(makeLabel(it, place = LabelPosition.BTM_RIGHT, isSelected = isSelected))
        }

        return IconGenerator(this).apply {
            val wrapper = ContextThemeWrapper(
                    this@MapActivity,
                    when {
                        fat.type == PlaceType.PERSON && isSelected -> R.style.MarkerRed_Selected
                        fat.type == PlaceType.PERSON && !isSelected -> R.style.MarkerRed
                        fat.type == PlaceType.EMPTY && isSelected -> R.style.MarkerGrey_Selected
                        fat.type == PlaceType.EMPTY && !isSelected -> R.style.MarkerGrey
                        fat.type == PlaceType.BUILDING && isSelected -> R.style.MarkerPink_Selected
                        fat.type == PlaceType.BUILDING && !isSelected -> R.style.MarkerPink
                        isSelected -> R.style.MarkerBlue_Selected
                        !isSelected -> R.style.MarkerBlue
                        else -> R.style.MarkerBlue
                    }
            )
            setBackground(
                    VectorDrawableCompat.create(
                            resources,
                            if (isSelected) R.drawable.ic_pin_fat_selected else R.drawable.ic_pin_fat,
                            wrapper.theme
                    )
            )
            setContentView(view)
        }
    }


    private suspend fun makeLabel(
            model: FatLabel,
            place: LabelPosition,
            isSelected: Boolean
    ): View? {

        val label = this.inflate(R.layout.view_label)

        val image = loadImage(model.imageUrl)

        when {
            image != null -> {
                label.findViewById<ImageView>(R.id.image).setImageDrawable(image)
                label.findViewById<TextView>(R.id.text).gone()
            }
            !model.text.isNullOrBlank() -> {
                label.findViewById<ImageView>(R.id.image).gone()
                label.findViewById<TextView>(R.id.text).setTextSize(
                        TypedValue.COMPLEX_UNIT_SP, when {
                    model.text.trim().length > 2 && isSelected -> 10f
                    model.text.trim().length <= 2 && isSelected -> 13f
                    model.text.trim().length > 2 && !isSelected -> 12f
                    model.text.trim().length <= 2 && !isSelected -> 9f
                    else -> throw IllegalStateException("Unspecified label text size")
                }
                )
                label.findViewById<TextView>(R.id.text).text = model.text
            }
            model.icon > 0 -> {
                label.findViewById<ImageView>(R.id.image).setPadding(6, 6, 6, 6)
                label.findViewById<ImageView>(R.id.image).setImageResource(model.icon)
                label.findViewById<TextView>(R.id.text).gone()
            }
            else -> return null
        }
        ImageViewCompat.setImageTintList(
                label.findViewById(R.id.background),
                ColorStateList.valueOf(model.bgColor)
        )


        label.layoutParams = FrameLayout.LayoutParams(
                resources.getDimensionPixelOffset(if (isSelected) R.dimen.map_marker_label_size_selected else R.dimen.map_marker_label_size),
                resources.getDimensionPixelOffset(if (isSelected) R.dimen.map_marker_label_size_selected else R.dimen.map_marker_label_size)
        ).apply {
            gravity = when (place) {
                LabelPosition.TOP_LEFT -> Gravity.TOP or (Gravity.START)
                LabelPosition.TOP_RIGHT -> Gravity.TOP or (Gravity.END)
                LabelPosition.BTM_LEFT -> Gravity.BOTTOM.or(Gravity.START)
                LabelPosition.BTM_RIGHT -> Gravity.BOTTOM or (Gravity.END)
            }
        }

        return label
    }


    private fun googleServicesAvailable(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val isAvailable = apiAvailability.isGooglePlayServicesAvailable(applicationContext)
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true
        } else if (apiAvailability.isUserResolvableError(isAvailable)) {
            val dialog = apiAvailability.getErrorDialog(this, isAvailable, 0)
            dialog.show()
        } else {
            Toast.makeText(this, "Cant connect to google play services", Toast.LENGTH_LONG).show()
        }
        return false
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
            @ColorInt val bgColor: Int = Color.WHITE,
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

    fun Context.inflate(@LayoutRes layoutRes: Int): View =
            (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(layoutRes, null, false)


    fun View.gone() {
        if (visibility != View.GONE)
            visibility = View.GONE
    }

    fun View.visible() {
        if (visibility != View.VISIBLE)
            visibility = View.VISIBLE
    }
}