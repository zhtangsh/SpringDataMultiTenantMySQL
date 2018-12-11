package com.zhtangsh.SpringDataMultiTenantMySQL.master.repository;

import com.zhtangsh.SpringDataMultiTenantMySQL.master.model.MasterTenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Created by Zihan Eric Tang(mrzihan.tang@gmail.com) on 2018/12/11
 */
@Repository
public interface MasterTenantRepository extends JpaRepository<MasterTenant, Long> {
    @Query("select p from MasterTenant p where p.tenantId = :tenantId")
    MasterTenant findByTenantId(@Param("tenantId") String tenantId);
}
