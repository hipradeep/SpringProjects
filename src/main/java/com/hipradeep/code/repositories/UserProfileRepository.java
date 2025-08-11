package com.hipradeep.code.repositories;


import com.hipradeep.code.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile,Integer> {

    //Any custom method or query can be implemented here
}
