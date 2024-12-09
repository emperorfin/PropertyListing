package emperorfin.android.propertylisting.ui.component

import android.content.Context
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.palette.graphics.Palette
import com.skydoves.landscapist.palette.BitmapPalette
import emperorfin.android.propertylisting.R
import emperorfin.android.propertylisting.ui.model.property.PropertyUiModel


@Composable
fun PropertyListItem(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    property: PropertyUiModel,
    onClick: (PropertyUiModel) -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(height = dimensionResource(id = R.dimen.property_list_item_surface_height))
            .clickable(
                onClick = {
                    onClick(property)
                }
            ),
        color = MaterialTheme.colorScheme.onBackground
    ) {

        val resources = context.resources

        ConstraintLayout {
            val (image, box, title) = createRefs()

            var palette by remember { mutableStateOf<Palette?>(null) }

            NetworkImage(
                networkUrl = property.imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = dimensionResource(id = R.dimen.property_list_item_network_image_height))
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                    },
                bitmapPalette = BitmapPalette {
                    palette = it
                }
            )

            Crossfade(
                targetState = palette,
                modifier = Modifier
                    .height(height = dimensionResource(id = R.dimen.property_list_item_cross_fade_height))
                    .constrainAs(box) {
                        top.linkTo(image.bottom)
                        bottom.linkTo(parent.bottom)
                    }
            ) {

                val backgroundColor =
                    Color(it?.darkVibrantSwatch?.rgb ?: colorResource(id = R.color.ff00ff00).toArgb())

                Box(
                    modifier = Modifier
                        .background(backgroundColor)
                        .alpha(alpha = resources.getFloat(R.dimen.property_list_item_box_alpha))
                        .fillMaxSize()
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(alpha = resources.getFloat(R.dimen.property_list_item_column_alpha))
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.property_list_item_column_padding_horizontal),
                        vertical = dimensionResource(id = R.dimen.property_list_item_column_padding_vertical)
                    )
                    .constrainAs(title) {
                        top.linkTo(box.top)
                        bottom.linkTo(box.bottom)
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = property.name,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    maxLines = integerResource(id = R.integer.property_list_item_text_max_lines),
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )

                property.isFeatured?.let {
                    if (it)
                        Text(
                            text = stringResource(id = R.string.featured),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            maxLines = integerResource(id = R.integer.property_list_item_text_max_lines),
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Normal,
                            fontStyle = FontStyle.Italic
                        )
                }

                Text(
                    text = property.lowestPricePerNightInEurosWithCurrencySymbol,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    maxLines = integerResource(id = R.integer.property_list_item_text_max_lines_var2),
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${property.rating}/10.0",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    maxLines = integerResource(id = R.integer.property_list_item_text_max_lines_var2),
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}