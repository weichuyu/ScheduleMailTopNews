package com.yu.tools.repo;

import com.yu.tools.model.BatchGoogleNews_TW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchGoogleNews_TWRepo extends JpaRepository<BatchGoogleNews_TW,String>, JpaSpecificationExecutor<BatchGoogleNews_TW>,CrudRepository<BatchGoogleNews_TW,String> {
    List<BatchGoogleNews_TW> findByBatchid(String batchid);
}
