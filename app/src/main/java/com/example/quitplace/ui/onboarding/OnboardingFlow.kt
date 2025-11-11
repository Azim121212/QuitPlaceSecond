package com.example.quitplace.ui.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// Состояния онбординга
enum class OnboardingState {
    WELCOME,
    PRIVACY,
    AI_CONSENT,
    COMPLETED
}

@Composable
fun OnboardingFlow(
    onComplete: () -> Unit
) {
    var currentState by remember { mutableStateOf(OnboardingState.WELCOME) }

    when (currentState) {
        OnboardingState.WELCOME -> {
            WelcomeScreen(
                onContinue = {
                    currentState = OnboardingState.PRIVACY
                }
            )
        }

        OnboardingState.PRIVACY -> {
            PrivacyScreen(
                onContinue = {
                    currentState = OnboardingState.AI_CONSENT
                }
            )
        }

        OnboardingState.AI_CONSENT -> {
            AIConsentScreen(
                onAccept = {
                    // Сохраняем согласие пользователя (потом реализуем)
                    onComplete()
                },
                onDecline = {
                    // Пользователь отказался (потом реализуем)
                    onComplete()
                }
            )
        }

        OnboardingState.COMPLETED -> {
            // Переходим к основному приложению
            onComplete()
        }
    }
}

@Composable
fun OnboardingFlowPreview() {
    OnboardingFlow(onComplete = {})
}