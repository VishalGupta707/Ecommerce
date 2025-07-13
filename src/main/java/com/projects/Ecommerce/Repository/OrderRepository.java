package com.projects.Ecommerce.Repository;

import com.projects.Ecommerce.Model.Orders;
import com.projects.Ecommerce.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Long> {

    @Query("SELECT o from Orders o JOIN FETCH o.user")
    List<Orders>findAllOrdersWithUsers();

    List<Orders> findByUser(User user);
}
