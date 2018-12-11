package com.zhtangsh.SpringDataMultiTenantMySQL.tenant.repository;

import com.zhtangsh.SpringDataMultiTenantMySQL.tenant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Created by Zihan Eric Tang(mrzihan.tang@gmail.com) on 2018/12/10
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
