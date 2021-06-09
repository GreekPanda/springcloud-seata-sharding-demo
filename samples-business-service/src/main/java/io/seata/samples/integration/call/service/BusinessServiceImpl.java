package io.seata.samples.integration.call.service;

import io.seata.core.context.RootContext;
import io.seata.samples.integration.common.dto.BusinessDTO;
import io.seata.samples.integration.common.dto.CommodityDTO;
import io.seata.samples.integration.common.dto.OrderDTO;
import io.seata.samples.integration.common.dubbo.OrderDubboService;
import io.seata.samples.integration.common.dubbo.StorageDubboService;
import io.seata.samples.integration.common.enums.RspStatusEnum;
import io.seata.samples.integration.common.exception.DefaultException;
import io.seata.samples.integration.common.response.ObjectResponse;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @Author: lidong
 * @Description Dubbo业务发起方逻辑
 * @Date Created in 2019/9/5 18:36
 */
@Service(value = "businessService")
@Slf4j
public class BusinessServiceImpl implements BusinessService {

    @DubboReference(version = "1.0.0", timeout = 300000)
    private StorageDubboService storageDubboService;

    @DubboReference(version = "1.0.0", timeout = 300000)
    private OrderDubboService orderDubboService;

    boolean flag;

    /**
     * 处理业务逻辑 正常的业务逻辑
     *
     * @Param:
     * @Return:
     */
    @GlobalTransactional(timeoutMills = 300000, name = "dubbo-gts-seata-example")
    //@ShardingTransactionType(TransactionType.BASE)
    @Override
    public ObjectResponse handleBusiness(BusinessDTO businessDTO) {
        ObjectResponse objectResponse = this.doBussiness(businessDTO);
        return objectResponse;
    }

    /**
     * 出处理业务服务，出现异常回顾
     *
     * @param businessDTO
     * @return
     */
    @GlobalTransactional(timeoutMills = 300000, name = "dubbo-gts-seata-example")
    @Override
    public ObjectResponse handleBusiness2(BusinessDTO businessDTO) {
        ObjectResponse objectResponse = this.doBussiness(businessDTO);
        // 打开注释测试事务发生异常后，全局回滚功能
        if (!flag) {
            throw new RuntimeException("测试抛异常后，分布式事务回滚！");
        }
        return objectResponse;
    }


    private ObjectResponse doBussiness(BusinessDTO businessDTO) {
        TransactionTypeHolder.set(TransactionType.BASE);
        log.info("开始全局事务，XID = " + RootContext.getXID());
        ObjectResponse<Object> objectResponse = new ObjectResponse<>();
        //1、扣减库存
        CommodityDTO commodityDTO = new CommodityDTO();
        commodityDTO.setCommodityCode(businessDTO.getCommodityCode());
        commodityDTO.setCount(businessDTO.getCount());
        ObjectResponse storageResponse = storageDubboService.decreaseStorage(commodityDTO);
        //2、创建订单
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(businessDTO.getUserId());
        orderDTO.setCommodityCode(businessDTO.getCommodityCode());
        orderDTO.setOrderCount(businessDTO.getCount());
        orderDTO.setOrderAmount(businessDTO.getAmount());
        orderDTO.setCreateMonth(StringUtils.isNotBlank(businessDTO.getCreateMonth()) ? businessDTO.getCreateMonth() : LocalDate.now().format(DateTimeFormatter.ofPattern("YYYYMM")));
        ObjectResponse<OrderDTO> response = orderDubboService.createOrder(orderDTO);

        if (storageResponse.getStatus() != 200 || response.getStatus() != 200) {
            throw new DefaultException(RspStatusEnum.FAIL);
        }

        objectResponse.setStatus(RspStatusEnum.SUCCESS.getCode());
        objectResponse.setMessage(RspStatusEnum.SUCCESS.getMessage());
        objectResponse.setData(response.getData());
        return objectResponse;
    }
}
