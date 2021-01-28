package org.jeecg.modules.purchase.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.purchase.entity.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: 客户表
 * @Author: jeecg-boot
 * @Date:   2021-01-25
 * @Version: V1.0
 */
@Repository
public interface CustomerMapper extends BaseMapper<Customer> {

}
