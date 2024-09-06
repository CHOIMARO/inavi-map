@file:OptIn(ExperimentalMaterial3Api::class)

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inavi_map_compose.map.compose.InaviMapCompose
import com.example.inavi_map_compose.map.compose.InvMarker
import com.example.inavi_map_compose.map.compose.InvMarkerState
import com.example.inavi_map_compose.map.compose.MapApplier
import com.example.inavi_map_compose.map.compose.MapProperties
import com.example.inavi_map_compose.map.compose.MapUiSettings
import com.example.inavi_map_compose.map.compose.UserTrackingMode
import com.example.inavi_map_compose.map.compose.rememberFusedLocationSource
import com.example.toyproject.R
import com.example.toyproject.home.model.ClickType
import com.example.toyproject.home.model.MarkerInformationModel
import com.example.toyproject.home.viewmodel.MainViewModel
import com.example.toyproject.ui.theme.ToyProjectTypography
import com.inavi.mapsdk.maps.InaviMap
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    val viewModel = hiltViewModel<MainViewModel>()
    LaunchedEffect(Unit) {
        viewModel.setSdkAppKey()
    }
    val scaffoldState = rememberBottomSheetScaffoldState()
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            Box(
                contentAlignment = Alignment.Center
            ) {
                ShowBottomSheetContent(viewModel)
            }
        },
        sheetPeekHeight = 0.dp
    ) {
        Box(
            modifier = Modifier.padding(it),
            contentAlignment = Alignment.Center,
        ) {
            ShowInvMapView(viewModel, scaffoldState)
        }
    }
}

@Composable
fun ShowBottomSheetContent(viewModel: MainViewModel) {
    val detailedSearchData by viewModel.detailedSearchData.collectAsState()
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = detailedSearchData?.title ?: "",
                style = ToyProjectTypography.titleLarge
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = detailedSearchData?.address ?: "",
                    style = ToyProjectTypography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Button(
                onClick = {
                    //TODO function
                }
            ) {
                Text(text = stringResource(id = R.string.arrival))
            }
        }

    }
}

@Composable
fun ShowInvMapView(viewModel: MainViewModel, scaffoldState: BottomSheetScaffoldState) {
    val consumeDoubleClick = remember { mutableStateOf(false) }

    InaviMapCompose(
        locationProvider = rememberFusedLocationSource(
            isCompassEnabled = true
        ),
        mapProperties = MapProperties(
            userTrackingMode = UserTrackingMode.Tracking
        ),
        mapUiSettings = MapUiSettings(
            isLocationButtonEnabled = true
        ),
        onMapClick = { point, coord ->
            viewModel.setClickedMarkerState(InvMarkerState(coord, point), ClickType.CLICK)
        },
        onMapDoubleClick = { point, coord ->
            Log.e(">>>>>", "OnMapDoubleClick")
            consumeDoubleClick.value
        },
        onMapLongClick = { point, coord ->
            viewModel.setClickedMarkerState(InvMarkerState(coord, point), ClickType.LONG_CLICK)
        }
    ) {
        val mapApplier = currentComposer.applier as? MapApplier
        val map = mapApplier?.map ?: error("Error adding Marker")

        GetGeoCodingData(map, viewModel)
        RefineClickedMarker(map, viewModel)
        ShowMarker(viewModel = viewModel, scaffoldState = scaffoldState)
        GetProcessedMarker(viewModel)
    }
}

@Composable
fun RefineClickedMarker(map: InaviMap, viewModel: MainViewModel) {
    val clickedMarkerState by viewModel.clickedMarkerState.collectAsState()
    clickedMarkerState?.let {
        viewModel.setProcessedMarkerState(map)
    }
}

@Composable
private fun GetProcessedMarker(viewModel: MainViewModel) {
    val processedMarkerModel by viewModel.processedMarkerModel.collectAsState()
    processedMarkerModel?.let { processedMarkerModel ->
        when(processedMarkerModel.clickType) {
            ClickType.CLICK -> {
                if (processedMarkerModel.poiId != null) {
                    viewModel.getDetailedSearchData(processedMarkerModel.poiId)
                } else {
                    viewModel.setDetailedSearchData(null)
                }
            }
            ClickType.LONG_CLICK -> {
                viewModel.getReverseGeoCoding(processedMarkerModel.invMarkerState?.position?.longitude ?: 0.0, processedMarkerModel.invMarkerState?.position?.latitude ?: 0.0)
            }
            ClickType.DOUBLE_CLICK -> {

            }
            else -> {}
        }
    }

}

@Composable
private fun ShowMarker(
    viewModel: MainViewModel,
    scaffoldState: BottomSheetScaffoldState
) {
    val detailedSearchData by viewModel.detailedSearchData.collectAsState()
    val scope = rememberCoroutineScope()

    if (detailedSearchData != null) {
        when (detailedSearchData!!.clickType) {
            ClickType.CLICK -> {
                InvMarker(
                    state = detailedSearchData!!.invMarkerState ?: InvMarkerState(),
                    title = detailedSearchData!!.title ?: "알 수 없음"
                )
                LaunchedEffect(Unit) {
                    scope.launch {
                        scaffoldState.bottomSheetState.expand()
                    }
                }
            }
            ClickType.LONG_CLICK -> {
                if (detailedSearchData != null) {
                    InvMarker(
                        state = detailedSearchData!!.invMarkerState ?: InvMarkerState(),
                        title = detailedSearchData!!.title ?: "알 수 없음"
                    )
                    LaunchedEffect(Unit) {
                        scope.launch {
                            scaffoldState.bottomSheetState.expand()
                        }
                    }
                }            }
            ClickType.DOUBLE_CLICK -> {
            }
            null -> {}
        }
    } else {
        InvMarker(
            state = InvMarkerState(),
        )
        LaunchedEffect(Unit) {
            scope.launch {
                scaffoldState.bottomSheetState.show()
            }
        }
    }
}
@Composable
fun GetGeoCodingData(map: InaviMap, viewModel: MainViewModel) {
    val reverseGeoCodingData by viewModel.reverseGeoCodingData.collectAsState()
    reverseGeoCodingData?.let {
        viewModel.setDetailedSearchData(
            MarkerInformationModel(
                clickType = ClickType.LONG_CLICK,
                invMarkerState = reverseGeoCodingData?.invMarkerState,
                address = reverseGeoCodingData?.locationModel?.adm?.roadName + reverseGeoCodingData?.locationModel?.adm?.roadJibun,
                title = reverseGeoCodingData?.locationModel?.adm?.address + reverseGeoCodingData?.locationModel?.adm?.jibun,
                poiId = null
            )
        )
    }
}