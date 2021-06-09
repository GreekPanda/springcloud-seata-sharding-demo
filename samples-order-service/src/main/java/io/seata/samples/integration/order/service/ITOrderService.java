package io.seata.samples.integration.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.seata.samples.integration.common.dto.OrderDTO;
import io.seata.samples.integration.common.response.ObjectResponse;
import io.seata.samples.integration.order.entity.TOrder;

import java.util.List;

/**
 * <p>
 *  创建订单
 * </p>
 *
 * * @author lidong
 * @since 2019-09-04
 */
public interface ITOrderService extends IService<TOrder> {

    /**
     * 创建订单
     */
    ObjectResponse<OrderDTO> createOrder(OrderDTO orderDTO);
    /**
     * 查找订单
     */
    ObjectResponse<List<OrderDTO>> listOrderByCreateMonth(Integer startMonth, Integer endMonth);
}
