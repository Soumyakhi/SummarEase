package com.pdfai.pdfai.repository;

import com.pdfai.pdfai.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {
    public Users findByUid(Long id);
    public Users findByEmail(String email);
    @Query(value =
            "SELECT * FROM users WHERE uname LIKE ?2 AND uid != ?1 ORDER BY LENGTH(uname)",
            nativeQuery = true)
    public List<Users> findUsersByNameLike(long seekerId,String uname);

}
