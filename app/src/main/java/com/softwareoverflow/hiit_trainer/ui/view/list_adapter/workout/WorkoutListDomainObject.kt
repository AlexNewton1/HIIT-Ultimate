package com.softwareoverflow.hiit_trainer.ui.view.list_adapter.workout

import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO

data class WorkoutListDomainObject(val dto: WorkoutDTO, var isSelected: Boolean)