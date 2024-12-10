package emperorfin.android.propertylisting.ui.component

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.palette.BitmapPalette
import emperorfin.android.propertylisting.R
import emperorfin.android.propertylisting.ui.theme.ShimmerHighLight



/**
 * A wrapper around [CoilImage] setting a default [contentScale] and showing
 * an indicator when loading poster images.
 *
 * @see CoilImage https://github.com/skydoves/landscapist#coil
 */
@Composable
fun NetworkImage(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    networkUrl: Any?,
    circularReveal: CircularReveal? = null,
    contentScale: ContentScale = ContentScale.FillBounds,
    bitmapPalette: BitmapPalette? = null,
    shimmerParams: ShimmerParams? = ShimmerParams(
        baseColor = MaterialTheme.colorScheme.background,
        highlightColor = ShimmerHighLight,
        dropOff = context.resources.getFloat(R.dimen.network_image_shimmer_params_drop_off_0_65)
    ),
) {
    val url = networkUrl ?: return

    if (shimmerParams == null) {
        CoilImage(
            imageModel = url,
            modifier = modifier,
            contentScale = contentScale,
            circularReveal = circularReveal,
            bitmapPalette = bitmapPalette,
            failure = {
                Text(
                    text = stringResource(id = R.string.error_message_coil_image_request_failed),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxSize()
                )
            },
            loading = { LoadingIndicator() }
        )
    } else {
        CoilImage(
            imageModel = url,
            modifier = modifier,
            contentScale = contentScale,
            circularReveal = circularReveal,
            bitmapPalette = bitmapPalette,
            shimmerParams = shimmerParams,
            failure = {
                Text(
                    text = stringResource(id = R.string.error_message_coil_image_request_failed),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxSize()
                )
            }
        )
    }
}