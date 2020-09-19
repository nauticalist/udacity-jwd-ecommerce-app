package com.udacity.project4.domain.repository;

import com.udacity.project4.domain.model.Cart;
import com.udacity.project4.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUser(User user);
}
