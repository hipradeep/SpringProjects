package com.hipradeep.code.repositories;

import com.hipradeep.code.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    //Any custom method or query can be implemented here
}
