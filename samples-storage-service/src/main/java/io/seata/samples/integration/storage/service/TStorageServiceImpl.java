package io.seata.samples.integration.storage.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.samples.integration.common.dto.CommodityDTO;
import io.seata.samples.integration.common.enums.RspStatusEnum;
import io.seata.samples.integration.common.response.ObjectResponse;
import io.seata.samples.integration.storage.entity.TStorage;
import io.seata.samples.integration.storage.mapper.TStorageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 库存服务实现类
 * </p>
 * <p>
 * * @author lidong
 *
 * @since 2019-09-04
 */
@Service
@Slf4j
public class TStorageServiceImpl extends ServiceImpl<TStorageMapper, TStorage> implements ITStorageService {


    @Transactional
    @Override
    public ObjectResponse decreaseStorage(CommodityDTO commodityDTO) {
        TStorage tStorage = baseMapper.selectOne(new QueryWrapper<TStorage>().eq("commodity_code", commodityDTO.getCommodityCode()));
        ObjectResponse<Object> response = new ObjectResponse<>();

        if (tStorage == null) {
            throw new RuntimeException("查询记录为空");
        }

        if (tStorage.getCount() == null || tStorage.getCount() <= 0) {
            log.info("当前库存为： {}", tStorage.getCount());
            response.setStatus(RspStatusEnum.FAIL.getCode());
            response.setMessage(RspStatusEnum.FAIL.getMessage());
            return response;
        }

        int storage = baseMapper.decreaseStorage(tStorage.getId(), commodityDTO.getCount());

        if (storage > 0) {
            response.setStatus(RspStatusEnum.SUCCESS.getCode());
            response.setMessage(RspStatusEnum.SUCCESS.getMessage());
            return response;
        }

        response.setStatus(RspStatusEnum.FAIL.getCode());
        response.setMessage(RspStatusEnum.FAIL.getMessage());
        return response;
    }

    @Transactional
    @Override
    public ObjectResponse increaseStorage(CommodityDTO commodityDTO) {
        TStorage tStorage = baseMapper.selectOne(new QueryWrapper<TStorage>().eq("commodity_code", commodityDTO.getCommodityCode()));
        ObjectResponse<Object> response = new ObjectResponse<>();

        //如果记录为空则插入一条数据否则将原来的库存进行增加
        if (tStorage == null) {
            log.info("增加一条记录： {}", commodityDTO.getCommodityCode());
            TStorage tStorage1 = new TStorage();
            tStorage1.setCommodityCode(commodityDTO.getCommodityCode());
            tStorage1.setCount(commodityDTO.getCount());
            tStorage1.setName(commodityDTO.getName());

            int ret = baseMapper.createStorage(tStorage1);
            if (ret > 0) {
                response.setStatus(RspStatusEnum.SUCCESS.getCode());
                response.setMessage(RspStatusEnum.SUCCESS.getMessage());
                return response;
            } else {
                response.setStatus(RspStatusEnum.FAIL.getCode());
                response.setMessage(RspStatusEnum.FAIL.getMessage());
                return response;
            }
        } else {
            if (tStorage.getCount() == null || tStorage.getCount() <= 0) {
                log.info("当前库存为： {}", tStorage.getCount());
                response.setStatus(RspStatusEnum.FAIL.getCode());
                response.setMessage(RspStatusEnum.FAIL.getMessage());
                return response;
            }

            int storage = baseMapper.increaseStorage(tStorage.getId(), commodityDTO.getCount());

            if (storage > 0) {
                response.setStatus(RspStatusEnum.SUCCESS.getCode());
                response.setMessage(RspStatusEnum.SUCCESS.getMessage());
                return response;
            }

            response.setStatus(RspStatusEnum.FAIL.getCode());
            response.setMessage(RspStatusEnum.FAIL.getMessage());
            return response;
        }
    }

}
