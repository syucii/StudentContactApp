package com.example.studentcontactapp.database.dao

import androidx.room.*
import com.example.studentcontactapp.database.entity.StudentEntity

@Dao
interface StudentDao {

    @Insert
    suspend fun insert(student: StudentEntity)

    @Insert
    suspend fun insertAll(students: List<StudentEntity>)

    @Query("SELECT * FROM Student ORDER BY name ASC")
    suspend fun getAllStudents(): List<StudentEntity>

    @Query("SELECT * FROM Student WHERE id = :id")
    suspend fun getStudentById(id: Int): StudentEntity?

    @Query("""
        SELECT * FROM Student 
        WHERE name LIKE '%' || :keyword || '%' 
        OR nim LIKE '%' || :keyword || '%'
        ORDER BY name ASC
    """)
    suspend fun searchStudents(keyword: String): List<StudentEntity>

    @Update
    suspend fun update(student: StudentEntity)

    @Query("DELETE FROM Student WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT COUNT(*) FROM Student")
    suspend fun getStudentCount(): Int
}
