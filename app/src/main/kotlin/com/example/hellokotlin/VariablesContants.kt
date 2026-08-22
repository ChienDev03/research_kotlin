package com.example.hellokotlin

fun main() {
    // val: chỉ gán một lần -> ưu tiên val
    val projectName = "Kotlin Research"
    // var: có thể gán lại khi giá trị thực sự thay đổi
    var completedTasks = 2
    val totalTasks = 5
    completedTasks += 1
    println("Project: $projectName")
    println("Completed tasks: $completedTasks/$totalTasks")
    val remainingTasks = totalTasks - completedTasks
    val hasRemainingWork = true
    val isValidProgress = true
    val canContinue = hasRemainingWork && isValidProgress
}
