package domain

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable

sealed class Results<out T> {
    data object Loading : Results<Nothing>()
    data class Success<T>(val data: T) : Results<T>()
    data class Error(val error: String) : Results<Nothing>()

    fun getSuccessData() = (this as Success).data
    fun getSuccessDataOrNull(): T? {
        return try {
            (this as Success).data
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Returns an error message from an [Error]
     * @throws ClassCastException If the current state is not [Error]
     *  */
    fun getErrorMessage() = (this as Error).error
    fun getErrorMessageOrEmpty(): String {
        return try {
            (this as Error).error
        } catch (e: Exception) {
            ""
        }
    }


    @Composable
    fun DisplayResult(
        onLoading: @Composable () -> Unit,
        onSuccess: @Composable (T) -> Unit,
        onError: @Composable (String) -> Unit,
        transitionSpec: AnimatedContentTransitionScope<*>.() -> ContentTransform = {
            fadeIn(tween(durationMillis = 300)) togetherWith
                    fadeOut(tween(durationMillis = 300))
        }
    ) {
        AnimatedContent(
            targetState = this,
            transitionSpec = transitionSpec,
            label = "Animated State"
        ) { state ->
            when (state) {
                is Loading -> {
                    onLoading()
                }

                is Success -> {
                    onSuccess(state.getSuccessData())
                }

                is Error -> {
                    onError(state.getErrorMessage())
                }
            }
        }
    }
}