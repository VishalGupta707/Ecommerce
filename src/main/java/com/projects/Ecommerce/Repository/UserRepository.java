package com.projects.Ecommerce.Repository;

import com.projects.Ecommerce.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);
}
