import android.graphics.PointF
import android.graphics.RectF
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.toyproject.ui.compose.MapApplier
import com.example.toyproject.ui.compose.MapNode
import com.example.toyproject.util.DpToPx
import com.inavi.mapsdk.constants.InvConstants
import com.inavi.mapsdk.geometry.LatLng
import com.inavi.mapsdk.style.shapes.InvMarker
import com.inavi.mapsdk.style.shapes.InvImage

internal class InvMarkerNode(
    val overlay: InvMarker,
    val markerState: InvMarkerState,
    var onMarkerClick: (InvMarker) -> Boolean,
) : MapNode {
    override fun onAttached() {
        markerState.marker = overlay
    }

    override fun onRemoved() {
        markerState.marker = null
        overlay.map = null
    }

    override fun onCleared() {
        markerState.marker = null
        overlay.map = null
    }
}

object InvMarkerDefaults {
    const val GlobalZIndex: Int = InvMarker.DEFAULT_GLOBAL_Z_INDEX
    val Icon: InvImage = InvMarker.DEFAULT_ICON
    val Anchor: Offset = Offset(0.5f, 1.0f)
    val TitleTextSize: TextUnit = 14.sp
}

class InvMarkerState(
    position: LatLng? = LatLng(0.0, 0.0),
    point: PointF? = null
) {
    var position: LatLng? by mutableStateOf(position)
    var point: PointF? by mutableStateOf(point)

    private var markerState: MutableState<InvMarker?> = mutableStateOf(null)
    internal var marker: InvMarker?
        get() = markerState.value
        set(value) {
            if (markerState.value == null && value == null) return
            if (markerState.value != null && value != null) {
                error("InvMarkerState may only be associated with one InvMarker at a time.")
            }
            markerState.value = value
        }

    companion object {
        val Saver: Saver<InvMarkerState, Pair<LatLng?, PointF?>> = Saver(
            save = { Pair(it.position, it.point) },
            restore = { InvMarkerState(it.first, it.second) }
        )
    }
}

@Composable
fun rememberInvMarkerState(
    key: String? = null,
    position: LatLng? = LatLng(0.0, 0.0),
    point: PointF? = null
): InvMarkerState = rememberSaveable(key = key, saver = InvMarkerState.Saver) {
    InvMarkerState(position, point)
}

@Composable
fun InvMarker(
    state: InvMarkerState = rememberInvMarkerState(),
    icon: InvImage = InvMarkerDefaults.Icon,
    iconScale: Float = 1.0f,
    anchor: Offset = InvMarkerDefaults.Anchor,
    title: String = "",
    titleTextSize: TextUnit = InvMarkerDefaults.TitleTextSize,
    titleColor: Color = Color.Black,
    titleHaloColor: Color = Color.White,
    titleMaxWidth: Dp = Dp.Unspecified,
    titleMargin: Dp = 0.dp,
    alpha: Float = 1.0f,
    angle: Float = 0.0f,
    isIconFlat: Boolean = false,
    isTitleFlat: Boolean = false,
    isAllowOverlapMarkers: Boolean = false,
    isAllowOverlapTitle: Boolean = false,
    isAllowOverlapSymbols: Boolean = false,
    isTransitionEnabled: Boolean = true,
    visible: Boolean = true,
    zIndex: Int = 0,
    onClick: (InvMarker) -> Boolean = { false },
) {
    MarkerImpl(
        state = state,
        alpha = alpha,
        anchor = anchor,
        icon = icon,
        angle = angle,
        zIndex = zIndex,
        onClick = onClick,
        title = title,
        visible = visible
    )
}

@Composable
private fun MarkerImpl(
    state: InvMarkerState = rememberInvMarkerState(),
    icon: InvImage = InvMarkerDefaults.Icon,
    iconTintColor: Color = Color.Transparent,
    anchor: Offset = Offset(0.5f, 1.0f),
    alpha: Float = 1.0f,
    angle: Float = 0.0f,
    isFlat: Boolean = false,
    isHideCollidedSymbols: Boolean = false,
    isHideCollidedMarkers: Boolean = false,
    isHideCollidedCaptions: Boolean = false,
    isForceShowIcon: Boolean = false,
    isForceShowCaption: Boolean = false,
    isIconPerspectiveEnabled: Boolean = false,
    isCaptionPerspectiveEnabled: Boolean = false,
    tag: Any? = null,
    visible: Boolean = true,
    minZoom: Double = InvConstants.MINIMUM_ZOOM,
    minZoomInclusive: Boolean = true,
    maxZoom: Double = InvConstants.MAXIMUM_ZOOM,
    maxZoomInclusive: Boolean = true,
    zIndex: Int = 0,
    globalZIndex: Int = InvMarkerDefaults.GlobalZIndex,
    title: String = "",
    onClick: (InvMarker) -> Boolean = { false },
) {
    val mapApplier = currentComposer.applier as? MapApplier
    val density = LocalDensity.current

    ComposeNode<InvMarkerNode, MapApplier>(
        factory = {
            val map = mapApplier?.map ?: error("Error adding Marker")
            val overlay: InvMarker = InvMarker().apply {
                this.position = state.position
                this.anchor = PointF(anchor.x, anchor.y)
                this.alpha = alpha
                this.angle = angle
                this.title = title

                // Overlay
                this.tag = tag
                this.isVisible = visible
                this.zIndex = zIndex
                this.globalZIndex = globalZIndex
            }
            overlay.map = map
            overlay.isAllowOverlapMarkers = true
            overlay.iconScale = 0.5f
            overlay.setOnClickListener {
                mapApplier
                    .nodeForMarker(overlay)
                    ?.onMarkerClick
                    ?.invoke(overlay)
                    ?: false
            }
            InvMarkerNode(
                overlay = overlay,
                markerState = state,
                onMarkerClick = onClick,
            )
        },
        update = {
            // The node holds density so that the updater blocks can be non-capturing,
            // allowing the compiler to turn them into singletons
            val map = mapApplier?.map ?: error("Error adding Marker")
            update(onClick) { this.onMarkerClick = it }

            set(state.position) { this.overlay.position = it }
            set(anchor) { this.overlay.anchor = PointF(it.x, it.y) }
            set(icon) { this.overlay.iconImage = it }

            set(alpha) { this.overlay.alpha = it }
            set(angle) { this.overlay.angle = it }

            set(title) { this.overlay.title = it }

            // Overlay
            set(tag) { this.overlay.tag = it }
            set(visible) { this.overlay.isVisible = it }
            set(zIndex) { this.overlay.zIndex = it }
            set(globalZIndex) { this.overlay.globalZIndex = it }
        }
    )
}

@Composable
public fun InvMarkerComposable(
    vararg keys: Any,
    state: InvMarkerState = rememberInvMarkerState(),
    iconTintColor: Color = Color.Transparent,
    icon: InvImage = InvMarkerDefaults.Icon,
    alpha: Float = 1.0f,
    angle: Float = 0.0f,
    isFlat: Boolean = false,
    isHideCollidedSymbols: Boolean = false,
    isHideCollidedMarkers: Boolean = false,
    isHideCollidedCaptions: Boolean = false,
    isForceShowIcon: Boolean = false,
    isForceShowCaption: Boolean = false,
    isIconPerspectiveEnabled: Boolean = false,
    isCaptionPerspectiveEnabled: Boolean = false,
    tag: Any? = null,
    visible: Boolean = true,
    minZoom: Double = InvConstants.MINIMUM_ZOOM,
    minZoomInclusive: Boolean = true,
    maxZoom: Double = InvConstants.MAXIMUM_ZOOM,
    maxZoomInclusive: Boolean = true,
    zIndex: Int = 0,
    globalZIndex: Int = InvMarkerDefaults.GlobalZIndex,
    onClick: (InvMarker) -> Boolean = { false },
    content: @Composable () -> Unit,
) {
    MarkerImpl(
        state = state,
        alpha = alpha,
        isFlat = isFlat,
        icon = icon,
        iconTintColor = iconTintColor,
        angle = angle,
        isHideCollidedSymbols = isHideCollidedSymbols,
        isHideCollidedMarkers = isHideCollidedMarkers,
        isHideCollidedCaptions = isHideCollidedCaptions,
        isForceShowIcon = isForceShowIcon,
        isForceShowCaption = isForceShowCaption,
        isIconPerspectiveEnabled = isIconPerspectiveEnabled,
        isCaptionPerspectiveEnabled = isCaptionPerspectiveEnabled,
        tag = tag,
        visible = visible,
        minZoom = minZoom,
        minZoomInclusive = minZoomInclusive,
        maxZoom = maxZoom,
        maxZoomInclusive = maxZoomInclusive,
        zIndex = zIndex,
        globalZIndex = globalZIndex,
        onClick = onClick,
    )
}
