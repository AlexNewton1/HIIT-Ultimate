package com.softwareoverflow.hiit_trainer.ui.view.list_adapter.workout

import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO

data class WorkoutLoaderDomainObject(
    val dto: WorkoutDTO,
    val type: WorkoutLoaderDomainObjectType = WorkoutLoaderDomainObjectType.USER
) {
    companion object {
        // TODO string res
        val placeholderUnlocked = WorkoutLoaderDomainObject(
            WorkoutDTO(name = "Workout slot available on FREE mode"),
            WorkoutLoaderDomainObjectType.PLACEHOLDER_UNLOCKED
        )

        // TODO string res
        val placeholderLocked = WorkoutLoaderDomainObject(
            WorkoutDTO(name = "Upgrade to PRO to unlock unlimited save slots"),
            WorkoutLoaderDomainObjectType.PLACEHOLDER_LOCKED
        )
    }
}

/**
 * Enum types for the saved workout.
 * USER -> User created
 * PLACEHOLDER_UNLOCKED -> A placeholder showing an unlocked slot
 * PLACEHOLDER_LOCKED -> A placeholder showing a locked slot (upgrade to PRO version)
 */
enum class WorkoutLoaderDomainObjectType { USER, PLACEHOLDER_UNLOCKED, PLACEHOLDER_LOCKED }