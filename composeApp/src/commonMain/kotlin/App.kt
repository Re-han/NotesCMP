import screens.HomePageNotes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import di.initKoin
import domain.core.Preferences
import theme.darkScheme
import theme.lightScheme

@Composable
fun App(
    pref: Preferences
) {
    initKoin()

    val colors = mutableStateOf(
        if (isSystemInDarkTheme()) darkScheme else lightScheme
    )

    MaterialTheme(colorScheme = colors.value) {
        Navigator(HomePageNotes(pref)) {
            SlideTransition(it)
        }
    }
}
