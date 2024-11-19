package com.ssafy.Classik_Backend.repository

import com.ssafy.Classik_Backend.entity.Composer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ComposerRepository : JpaRepository<Composer, Int> {

    fun findByName(composer: String): Composer

    @Query("SELECT c FROM Composer c ORDER BY RAND() LIMIT 1")
    fun findRandomComposer(): Composer

}