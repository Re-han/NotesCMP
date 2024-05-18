import screens.HomePageNotes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import data.MongoDB
import data.viewmodels.HomeVM
import di.initKoin
import org.koin.dsl.module
import screens.NoteDetailScreen
import theme.darkScheme
import theme.lightScheme

@Composable
fun App() {
    initKoin()

    val colors = mutableStateOf(
        if (isSystemInDarkTheme()) darkScheme else lightScheme
    )

    MaterialTheme(colorScheme = colors.value) {
        Navigator(HomePageNotes()) {
            SlideTransition(it)
        }
    }
}
