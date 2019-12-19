package com.yu.tools.repo;

import com.yu.tools.model.BatchWeiboTop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchWeiboTopRepo extends JpaRepository<BatchWeiboTop,String>, JpaSpecificationExecutor<BatchWeiboTop>,CrudRepository<BatchWeiboTop,String> {
    List<BatchWeiboTop> findByBatchid(String batchid);
}
