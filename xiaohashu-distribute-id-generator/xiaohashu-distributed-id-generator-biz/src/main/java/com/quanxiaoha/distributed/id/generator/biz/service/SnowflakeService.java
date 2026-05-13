package com.quanxiaoha.distributed.id.generator.biz.service;

import com.quanxiaoha.distributed.id.generator.biz.core.IDGen;
import com.quanxiaoha.distributed.id.generator.biz.core.common.PropertyFactory;
import com.quanxiaoha.distributed.id.generator.biz.core.common.Result;
import com.quanxiaoha.distributed.id.generator.biz.core.common.ZeroIDGen;
import com.quanxiaoha.distributed.id.generator.biz.constant.Constants;
import com.quanxiaoha.distributed.id.generator.biz.core.common.InitException;
import com.quanxiaoha.distributed.id.generator.biz.core.snowflake.SnowflakeIDGenImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service("SnowflakeService")
public class SnowflakeService {
    private Logger logger = LoggerFactory.getLogger(SnowflakeService.class);

    private IDGen idGen;

    public SnowflakeService() throws InitException {
        Properties properties = PropertyFactory.getProperties();
        boolean flag = Boolean.parseBoolean(properties.getProperty(Constants.LEAF_SNOWFLAKE_ENABLE, "true"));
        if (flag) {
            try {
                String zkAddress = properties.getProperty(Constants.LEAF_SNOWFLAKE_ZK_ADDRESS);
                int port = Integer.parseInt(properties.getProperty(Constants.LEAF_SNOWFLAKE_PORT));
                idGen = new SnowflakeIDGenImpl(zkAddress, port);
                if(idGen.init()) {
                    logger.info("Snowflake Service Init Successfully");
                } else {
                    logger.warn("Snowflake Service Init Fail, fallback to ZeroIDGen");
                    idGen = new ZeroIDGen();
                }
            } catch (Exception e) {
                logger.warn("Snowflake Service Init Exception, fallback to ZeroIDGen: {}", e.getMessage());
                idGen = new ZeroIDGen();
            }
        } else {
            idGen = new ZeroIDGen();
            logger.info("Zero ID Gen Service Init Successfully");
        }
    }

    public Result getId(String key) {
        return idGen.get(key);
    }
}
