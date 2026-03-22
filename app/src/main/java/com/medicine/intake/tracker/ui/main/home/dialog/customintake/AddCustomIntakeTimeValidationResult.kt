package com.medicine.intake.tracker.ui.main.home.dialog.customintake

sealed interface AddCustomIntakeTimeValidationResult {
    data object Success : AddCustomIntakeTimeValidationResult
    data class BadCustomTimeTime(val variant: BadCustomTimeVariant) :
        AddCustomIntakeTimeValidationResult {
        companion object {
            val InvalidFormat = BadCustomTimeTime(BadCustomTimeVariant.InvalidFormat)
            val OutOfRange = BadCustomTimeTime(BadCustomTimeVariant.OutOfRange)
        }
    }

}