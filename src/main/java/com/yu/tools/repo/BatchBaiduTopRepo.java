package com.yu.tools.repo;

import com.yu.tools.model.BatchBaiduTop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchBaiduTopRepo extends JpaRepository<BatchBaiduTop,String>, JpaSpecificationExecutor<BatchBaiduTop>,CrudRepository<BatchBaiduTop,String> {
    List<BatchBaiduTop> findByBatchid(String batchid);
}
