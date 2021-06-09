package io.seata.samples.integration.storage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.math.BigInteger;


/**
 * <p>
 *
 * </p>
 *
 * * @author lidong
 * @since 2019-09-04
 */
public class TStorage extends Model<TStorage> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private BigInteger id;
    private String commodityCode;
    private String name;
    private Integer count;


    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TStorage{" +
        ", id=" + id +
        ", commodityCode=" + commodityCode +
        ", name=" + name +
        ", count=" + count +
        "}";
    }
}
