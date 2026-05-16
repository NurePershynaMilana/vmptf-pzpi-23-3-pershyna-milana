package com.example.notebookapp.ui.journal

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.notebookapp.ui.journal.attendance.AttendanceFragment
import com.example.notebookapp.ui.journal.lessons.LessonsFragment
import com.example.notebookapp.ui.journal.students.StudentsFragment

class JournalPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> StudentsFragment()
        1 -> LessonsFragment()
        2 -> AttendanceFragment()
        else -> throw IllegalArgumentException("Unknown tab position: $position")
    }
}
