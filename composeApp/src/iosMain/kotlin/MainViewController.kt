import androidx.compose.ui.window.ComposeUIViewController
import domain.core.Preferences

fun MainViewController() = ComposeUIViewController {
    App(Preferences())
}