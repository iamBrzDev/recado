package app.recado.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import app.recado.R

/** Una sola familia en toda la app. */
val Poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
)

private val base = Typography()

val RecadoTypography = Typography(
    displayLarge = base.displayLarge.copy(fontFamily = Poppins),
    displayMedium = base.displayMedium.copy(fontFamily = Poppins),
    displaySmall = base.displaySmall.copy(fontFamily = Poppins),
    headlineLarge = base.headlineLarge.copy(fontFamily = Poppins),
    headlineMedium = base.headlineMedium.copy(fontFamily = Poppins),
    headlineSmall = base.headlineSmall.copy(fontFamily = Poppins),
    titleLarge = base.titleLarge.copy(fontFamily = Poppins),
    titleMedium = base.titleMedium.copy(fontFamily = Poppins),
    titleSmall = base.titleSmall.copy(fontFamily = Poppins),
    bodyLarge = base.bodyLarge.copy(fontFamily = Poppins),
    bodyMedium = base.bodyMedium.copy(fontFamily = Poppins),
    bodySmall = base.bodySmall.copy(fontFamily = Poppins),
    labelLarge = base.labelLarge.copy(fontFamily = Poppins),
    labelMedium = base.labelMedium.copy(fontFamily = Poppins),
    labelSmall = base.labelSmall.copy(fontFamily = Poppins),
)

/** Estilos propios que se repiten en varias pantallas. */
object RecadoText {
    val brandMark = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 88.sp,
        lineHeight = 92.sp,
    )
    val wordmark = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.2.sp,
    )
    val screenTitle = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 30.sp,
        lineHeight = 36.sp,
    )
    val headline = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp,
        lineHeight = 32.sp,
    )
    val meta = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    )
    val row = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 22.sp,
    )
    val field = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    )
    val input = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 30.sp,
    )
    val action = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
    )
}
