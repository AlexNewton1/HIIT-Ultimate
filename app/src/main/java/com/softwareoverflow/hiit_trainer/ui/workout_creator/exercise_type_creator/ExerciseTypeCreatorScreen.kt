@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.softwareoverflow.hiit_trainer.ui.workout_creator.exercise_type_creator

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.ui.theme.AppTheme
import com.softwareoverflow.hiit_trainer.ui.theme.etColors
import com.softwareoverflow.hiit_trainer.ui.theme.spacing
import com.softwareoverflow.hiit_trainer.ui.utils.compose.AppScreen
import com.softwareoverflow.hiit_trainer.ui.utils.compose.BottomAppRow
import com.softwareoverflow.hiit_trainer.ui.utils.compose.TopAppRow
import com.softwareoverflow.hiit_trainer.ui.utils.compose.etIcons
import com.softwareoverflow.hiit_trainer.ui.utils.compose.getDrawableId
import com.softwareoverflow.hiit_trainer.ui.utils.compose.pxToDp
import kotlin.math.abs
import android.graphics.Color as GraphicsColor


@Destination
@Composable
fun ExerciseTypeCreatorScreen(
    exerciseTypeDTO: ExerciseTypeDTO,
    navigator: DestinationsNavigator,
    resultBackNavigator: ResultBackNavigator<Long>,
    exerciseTypeViewModel: ExerciseTypeViewModel = viewModel(
        factory = ExerciseTypeViewModelFactory(
            LocalContext.current, exerciseTypeDTO
        )
    )
) {
    val colorHex = remember { mutableStateOf(exerciseTypeDTO.colorHex) }
    val iconName = remember { mutableStateOf(exerciseTypeDTO.iconName) }

    val etName = remember { mutableStateOf(exerciseTypeDTO.name ?: "") }

    val selectedColorId = remember { mutableStateOf(0) }
    val selectedIconId = remember { mutableStateOf(0) }


    AppScreen(topAppRow = {
        TopAppRow(
            startIcon = Icons.Filled.ArrowBack,
            onStartPressed = { navigator.popBackStack() },
            title = stringResource(id = R.string.create_new_exercise_type),
            endIcon = null,
            onEndIconPressed = null
        )
    }, bottomAppRow = {

        val isError = etName.value.isBlank() || etName.value.isEmpty() || etName.value.length > 30

        BottomAppRow {
            BasicTextField(
                value = etName.value, onValueChange = {
                    if (it.length <= 30) {
                        etName.value = it
                    }
                }, modifier = Modifier.weight(1f)
            ) { innerTextField ->

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.extraSmall)
                ) {
                    Box {
                        if (etName.value.isEmpty()) {
                            Text(
                                stringResource(id = R.string.exercise_type_name),
                                style = MaterialTheme.typography.overline
                            )
                        }
                        innerTextField()
                    }
                    Divider(
                        Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.spacing.extraSmall),
                        color = if (isError) MaterialTheme.colors.error else MaterialTheme.colors.onSurface,
                        thickness = 1.dp
                    )
                }
            }

            val content = LocalContext.current
            FloatingActionButton(
                onClick = {
                    if (!isError) {
                        val id = exerciseTypeViewModel.createOrUpdateExerciseType(
                            content, etName.value, selectedIconId.value, selectedColorId.value
                        )

                        resultBackNavigator.navigateBack(result = id)
                    }
                }, backgroundColor = if (isError) Color.Gray else MaterialTheme.colors.secondary
            ) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = stringResource(id = R.string.save),
                    Modifier.padding(MaterialTheme.spacing.small)
                )
            }
        }
    }, showBannerAd = true) { modifier ->
        ExerciseTypeCreatorContent(colorHex.value, iconName.value, onColorChange = {
            selectedColorId.value = it
        }, onIconChange = {
            selectedIconId.value = it
        }, modifier = modifier
        )
    }
}

@Composable
private fun ExerciseTypeCreatorContent(
    colorHex: String?,
    iconName: String?,
    onColorChange: (Int) -> Unit,
    onIconChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = etColors
    val icons = etIcons

    val startColor = if (colorHex != null) colors.indexOfFirst {
        it.toArgb() == Color(
            GraphicsColor.parseColor(colorHex)
        ).toArgb()
    } else 0

    onColorChange(etColors[startColor].toArgb())

    val startIcon = if (iconName != null) icons.indexOfFirst {
        it == getDrawableId(iconName, LocalContext.current)
    } else 0

    onIconChange(etIcons[startIcon])


    var size by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged {
                size = it
            }, contentAlignment = Alignment.Center
    ) {
        MiddlePager(
            orientation = Orientation.Horizontal,
            screenSize = DpSize(width = size.width.pxToDp(), height = size.height.pxToDp()),
            items = colors,
            startPosition = startColor,
            onItemChange = {
                etColors[it].toArgb()
            },
            Modifier.fillMaxWidth()
        ) { color, scale ->
            Box(
                Modifier
                    .size(MaterialTheme.spacing.extraExtraExtraLarge * scale)
                    .background(
                        color = color,
                        shape = CircleShape.copy(CornerSize(MaterialTheme.spacing.extraExtraExtraLarge * scale))
                    ), contentAlignment = Alignment.Center
            ) {}
        }

        MiddlePager(
            orientation = Orientation.Vertical,
            screenSize = DpSize(width = size.width.pxToDp(), height = size.height.pxToDp()),
            items = icons,
            startPosition = startIcon,
            onItemChange = {
                onIconChange(etIcons[it])
            },
            Modifier.fillMaxHeight()
        ) { icon, scale ->
            Icon(
                painterResource(id = icon),
                contentDescription = null,
                Modifier
                    .size(MaterialTheme.spacing.extraExtraExtraLarge * scale)
                    .padding(MaterialTheme.spacing.medium),
                tint = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@Composable
private fun <T> MiddlePager(
    orientation: Orientation,
    screenSize: DpSize,
    items: List<T>,
    startPosition: Int,
    onItemChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (T, Float) -> Unit
) {

    val itemsCount = items.size

    val numPages = Int.MAX_VALUE / itemsCount
    val startPage = numPages / 2
    val startIndex = (startPage * itemsCount) + startPosition

    var currentItem by remember { mutableStateOf(startPosition) }

    val pagerState = rememberPagerState(initialPage = startIndex)

    LaunchedEffect(pagerState) {
        // Collect from the a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            currentItem = page
            onItemChange(currentItem % itemsCount)
        }
    }

    if (orientation == Orientation.Horizontal) HorizontalPager(
        pageCount = Int.MAX_VALUE,
        modifier = modifier,
        state = pagerState,
        pageSize = PageSize.Fixed(MaterialTheme.spacing.extraExtraExtraLarge),
        beyondBoundsPageCount = 2,
        verticalAlignment = Alignment.CenterVertically
    ) { index ->
        MiddlePagerContent(
            offset = DpOffset(
                x = screenSize.width / 2f - MaterialTheme.spacing.extraExtraExtraLarge / 2f,
                y = 0.dp
            ),
            items = items,
            index = index,
            currentItem = currentItem,
            pagerState = pagerState,
            content = content
        )
    }
    else if (orientation == Orientation.Vertical) VerticalPager(
        pageCount = Int.MAX_VALUE,
        modifier = modifier,
        state = pagerState,
        pageSize = PageSize.Fixed(MaterialTheme.spacing.extraExtraExtraLarge),
        beyondBoundsPageCount = 2,
        horizontalAlignment = Alignment.CenterHorizontally
    ) { index ->
        MiddlePagerContent(
            offset = DpOffset(
                x = 0.dp, y = (screenSize.height - MaterialTheme.spacing.extraExtraExtraLarge) / 2f
            ),
            items = items,
            index = index,
            currentItem = currentItem,
            pagerState = pagerState,
            content = content
        )
    }
}

@Composable
private fun <T> MiddlePagerContent(
    offset: DpOffset,
    items: List<T>,
    index: Int,
    currentItem: Int,
    pagerState: PagerState,
    content: @Composable (T, Float) -> Unit
) {
    val item = items[index % items.size]

    var posFromMiddle = (index - currentItem).toFloat()

    posFromMiddle -= pagerState.currentPageOffsetFraction


    var scale = 0.8f - abs(posFromMiddle) * 0.2f
    // Some safety error checking to keep it within the bounds
    if (abs(posFromMiddle) > 2.5f) scale = 0f
    if (scale <= 0f) scale = 0f
    if (scale >= 0.8f) scale = 0.8f

    Box(
        modifier = Modifier
            .size(MaterialTheme.spacing.extraExtraExtraLarge)
            .offset(x = offset.x, y = offset.y), contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .scale(scale)
        ) {
            content(item, 1f)
        }
    }
}

@Preview(name = "Tablet", device = Devices.PIXEL_C)
@Preview(name = "Phone", device = Devices.PIXEL_4_XL)
@Composable
private fun Preview() {
    AppTheme {
        ExerciseTypeCreatorContent("#666666", null, {}, {})
    }
}