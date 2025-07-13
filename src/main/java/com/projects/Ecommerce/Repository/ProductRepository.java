package com.projects.Ecommerce.Repository;

import com.projects.Ecommerce.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
