package com.softwareoverflow.hiit_trainer.ui.view.list_adapter.exercise_type

import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO

data class ExerciseTypeListDomainObject (val dto: ExerciseTypeDTO, var isSelected: Boolean)