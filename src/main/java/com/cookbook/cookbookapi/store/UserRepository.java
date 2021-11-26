package com.cookbook.cookbookapi.store;

import com.cookbook.cookbookapi.domain.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserData, Integer> {

    UserData findUserByLoginAndPassword(String login, String password);
}
