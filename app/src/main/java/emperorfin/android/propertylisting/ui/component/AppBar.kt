package emperorfin.android.propertylisting.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import emperorfin.android.propertylisting.R
import emperorfin.android.propertylisting.ui.theme.Purple40


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String?,
    onBackPress: (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit) = {}
) {
    TopAppBar(
        modifier = Modifier.height(height = dimensionResource(id = R.dimen.app_bar_top_app_bar_height_70)),
        title = {
            Box {
                Text(
                    modifier = Modifier
                        .padding(all = dimensionResource(id = R.dimen.app_bar_text_padding_all_8))
                        .align(Alignment.TopCenter),
                    text = title ?: stringResource(id = R.string.empty),
                    color = Color.White,
                    fontSize = dimensionResource(id = R.dimen.app_bar_text_font_size_18).value.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = integerResource(id = R.integer.app_bar_text_max_lines_1),
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        navigationIcon = {
            onBackPress?.let {
                IconButton(onClick = { it() }) {
                    Box {
                        Image(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            colorFilter = ColorFilter.tint(Color.White),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Purple40
        ),
        actions = {
            actions()
        }
    )
}

@Composable
fun PropertiesScreenMenu(
    onNetworkStat: () -> Unit
) {
    TopAppBarDropdownMenu(
        iconContent = {
            Icon(Icons.Filled.MoreVert, stringResource(id = R.string.menu_more))
        }
    ) { closeMenu ->
        DropdownMenuItem(
            onClick = { onNetworkStat(); closeMenu() },
            text = { Text(text = stringResource(id = R.string.menu_item_network_stat)) }
        )
    }
}

@Composable
fun PropertyDetailsScreenMenu(
    onEurosPriceClick: () -> Unit,
    onDollarsPriceClick: () -> Unit,
    onPoundsPriceClick: () -> Unit,
) {
    TopAppBarDropdownMenu(
        iconContent = {
            Icon(
                painterResource(id = R.drawable.ic_filter_list),
                stringResource(id = R.string.menu_filter)
            )
        }
    ) { closeMenu ->
        DropdownMenuItem(
            onClick = { onEurosPriceClick(); closeMenu() },
            text = { Text(text = stringResource(id = R.string.menu_item_euro_price)) }
        )

        DropdownMenuItem(
            onClick = { onDollarsPriceClick(); closeMenu() },
            text = { Text(text = stringResource(id = R.string.menu_item_dollar_price)) }
        )

        DropdownMenuItem(
            onClick = { onPoundsPriceClick(); closeMenu() },
            text = { Text(text = stringResource(id = R.string.menu_item_pound_price)) }
        )
    }
}

@Composable
private fun TopAppBarDropdownMenu(
    iconContent: @Composable () -> Unit,
    content: @Composable ColumnScope.(() -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
        IconButton(onClick = { expanded = !expanded }) {
            iconContent()
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentSize(Alignment.TopEnd)
        ) {
            content { expanded = !expanded }
        }
    }
}