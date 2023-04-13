package com.example.a7minuteworkout

object Constants {

    fun defaultExerciseList(): ArrayList<ExerciseModel>
    {
        val exerciseList= ArrayList<ExerciseModel>()


        val jumpingJacks= ExerciseModel(1, "Jumping Jacks", R.drawable.jumping_jack, false, false)
        exerciseList.add(jumpingJacks)

        val fishPose= ExerciseModel(2, "Fish Pose", R.drawable.fish_pose, false, false)
        exerciseList.add(fishPose)

        val invertedVPlank= ExerciseModel(3, "Inverted V Plank", R.drawable.inverted_v_plank, false, false)
        exerciseList.add(invertedVPlank)

        val lunge= ExerciseModel(4, "Lunge", R.drawable.lunge, false, false)
        exerciseList.add(lunge)

        val pushUp= ExerciseModel(5, "Push Up", R.drawable.pushup, false, false)
        exerciseList.add(pushUp)

        val sidePlank= ExerciseModel(6, "Side Plank", R.drawable.side_plank, false, false)
        exerciseList.add(sidePlank)

        val squat= ExerciseModel(7, "Squat", R.drawable.squat, false, false)
        exerciseList.add(squat)

        val stepUpOnTheChair= ExerciseModel(8, "Step Up On The Chair", R.drawable.step_up_onto_chair, false, false)
        exerciseList.add(stepUpOnTheChair)

        val tricepsDipOnChair= ExerciseModel(9, "Triceps Dip On Chair", R.drawable.triceps_dip_on_chair, false, false)
        exerciseList.add(tricepsDipOnChair)

        val wallSit= ExerciseModel(10, "Wall Sit", R.drawable.wall_sit, false, false)
        exerciseList.add(wallSit)

        val highKneesRunningPlace= ExerciseModel(11, "High Knees Running Place", R.drawable.high_knees_running_place, false, false)
        exerciseList.add(highKneesRunningPlace)


        return exerciseList
    }
}