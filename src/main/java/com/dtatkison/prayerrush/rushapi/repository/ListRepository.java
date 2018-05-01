package com.dtatkison.prayerrush.rushapi.repository;

import com.dtatkison.prayerrush.rushapi.model.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Repository
public interface ListRepository extends JpaRepository<List, Integer> {
    Optional<List> findByListId(int id);

    @Modifying(clearAutomatically = true)
    @Transactional()
    @Query("UPDATE List l SET l.name = :name, l.description = :description, l.date = :date where l.listId = :listId")
    int updateList(@Param("name") String name, @Param("description") String description, @Param("date") Date date, @Param("listId") Integer listId);
}
