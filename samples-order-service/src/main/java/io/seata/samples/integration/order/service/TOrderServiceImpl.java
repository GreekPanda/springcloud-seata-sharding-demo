package io.seata.samples.integration.order.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;


import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.samples.integration.common.dto.AccountDTO;
import io.seata.samples.integration.common.dto.OrderDTO;
import io.seata.samples.integration.common.dubbo.AccountDubboService;
import io.seata.samples.integration.common.enums.RspStatusEnum;
import io.seata.samples.integration.common.response.ObjectResponse;
import io.seata.samples.integration.order.entity.TOrder;
import io.seata.samples.integration.order.mapper.TOrderMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 * <p>
 * * @author lidong
 *
 * @since 2019-09-04
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements ITOrderService {

    @DubboReference(version = "1.0.0", timeout = 30000)
    private AccountDubboService accountDubboService;

    /**
     * 创建订单
     *
     * @Param: OrderDTO  订单对象
     * @Return: OrderDTO  订单对象
     */
    @Override
    public ObjectResponse<OrderDTO> createOrder(OrderDTO orderDTO) {
        ObjectResponse<OrderDTO> response = new ObjectResponse<>();
        //扣减用户账户
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUserId(orderDTO.getUserId());
        accountDTO.setAmount(orderDTO.getOrderAmount());
        ObjectResponse objectResponse = accountDubboService.decreaseAccount(accountDTO);

        //生成订单号
        orderDTO.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
        //生成订单
        TOrder tOrder = new TOrder();
        BeanUtils.copyProperties(orderDTO, tOrder);
        tOrder.setCount(orderDTO.getOrderCount());
        tOrder.setAmount(orderDTO.getOrderAmount().doubleValue());
        tOrder.setId(IdWorker.getIdStr());
        tOrder.setCreateMonth(Integer.valueOf(orderDTO.getCreateMonth()));
        try {
            baseMapper.createOrder(tOrder);
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus(RspStatusEnum.FAIL.getCode());
            response.setMessage(RspStatusEnum.FAIL.getMessage());
            return response;
        }

        if (objectResponse.getStatus() != 200) {
            response.setStatus(RspStatusEnum.FAIL.getCode());
            response.setMessage(RspStatusEnum.FAIL.getMessage());
            return response;
        }

        response.setStatus(RspStatusEnum.SUCCESS.getCode());
        response.setMessage(RspStatusEnum.SUCCESS.getMessage());
        return response;
    }

    @Override
    public ObjectResponse<List<OrderDTO>> listOrderByCreateMonth(Integer startMonth, Integer endMonth) {
        List<OrderDTO> orderDTOS = baseMapper.listOrderByCreateMonth(startMonth, endMonth);
        ObjectResponse<List<OrderDTO>> objectObjectResponse = new ObjectResponse<List<OrderDTO>>();
        objectObjectResponse.setData(orderDTOS);
        return objectObjectResponse;
    }
}
