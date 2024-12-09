package emperorfin.android.propertylisting.ui.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import emperorfin.android.propertylisting.R
import emperorfin.android.propertylisting.ui.theme.PropertyListingTheme
import emperorfin.android.propertylisting.ui.theme.Teal200


@Composable
fun EmptyContent(
    @StringRes errorLabel: Int = R.string.content_description_error_message,
    @DrawableRes errorIcon: Int = R.drawable.im_oops,
    onRetry: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.size(size = dimensionResource(id = R.dimen.empty_content_image_size)),
                painter = painterResource(errorIcon),
                contentDescription = stringResource(R.string.content_description_error_message),
            )
            Text(
                text = stringResource(errorLabel),
                color = Color.Black,
                fontSize = dimensionResource(id = R.dimen.empty_content_text_font_size).value.sp,
                textAlign = TextAlign.Center,
            )
            Button(
                onClick = { onRetry() }, colors = ButtonDefaults.buttonColors(
                    containerColor = Teal200,
                    contentColor = Color.White
                )
            ) {
                Text(stringResource(R.string.btn_retry))
            }
        }
    }
}


@Preview
@Composable
private fun EmptyContentPreview() {
    PropertyListingTheme {
        Surface {
            EmptyContent(
                onRetry = {}
            )
        }
    }
}