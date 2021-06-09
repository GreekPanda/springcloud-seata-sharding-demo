package io.seata.samples.integration.order.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * 订单表分片的精确匹配算法： 区别于inline中的expression表达式，此方法更加灵活
 *
 * @author huangchangjin
 */
@Slf4j
public class OrderPreciseShardingTableAlgorithm implements PreciseShardingAlgorithm<Integer> {

    @Override
    public String doSharding(final Collection<String> tableNames, final PreciseShardingValue<Integer> shardingValue) {
        log.info("tableNames:{}\t shardingValue:{}", tableNames, shardingValue);
        StringBuilder sb = new StringBuilder();
        //获取设置的虚拟表名称，这里获取到的logicTableName=t_order
        String logicTableName = shardingValue.getLogicTableName();
        //拼接实际的表名称: t_order_202007
        sb.append(logicTableName)
                .append("_")
                .append(shardingValue.getValue());
        if (tableNames.contains(sb.toString())) {
            return sb.toString();
        } else {
            log.info("精确分片策略：没找到与分片键匹配的表名! {} : {} = {}", shardingValue.getLogicTableName(), shardingValue.getColumnName(), shardingValue.getValue());
            throw new UnsupportedOperationException();
        }
    }
}
