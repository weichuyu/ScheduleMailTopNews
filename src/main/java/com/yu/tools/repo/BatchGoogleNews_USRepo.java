package com.yu.tools.repo;

import com.yu.tools.model.BatchGoogleNews_TW;
import com.yu.tools.model.BatchGoogleNews_US;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchGoogleNews_USRepo extends JpaRepository<BatchGoogleNews_US,String>, JpaSpecificationExecutor<BatchGoogleNews_US>,CrudRepository<BatchGoogleNews_US,String> {
    List<BatchGoogleNews_US> findByBatchid(String batchid);
}
