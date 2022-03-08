package com.softwareoverflow.hiit_trainer.ui.view.list_adapter.workout

import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO

/**
 * Class used for containing
 */
class WorkoutListPlaceholder {
    companion object {
        val placeholderUnlocked by lazy { WorkoutOverwriteDomainObject(
            WorkoutDTO(null, "Workout slot available for FREE"),
            false
        ) }
    }
}