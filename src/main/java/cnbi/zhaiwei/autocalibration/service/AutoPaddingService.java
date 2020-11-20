package cnbi.zhaiwei.autocalibration.service;

import java.util.List;

public interface AutoPaddingService {
    /**
     * 自动将一个指定表中有汉语备注的维度字段
     * 及相关信息作为数据插入到dw_compose表中
     * @param tableName 指定表的表名称
     */
    void autoPaddingCompose(String tableName);

    /**
     * 批量将有特定前缀的一系列表中有汉语备注的维度
     * 字段及相关信息作为数据插入到dw_compose表中
     * @param tablePrefix 指定的表前缀
     */
    void batchPaddingCompose(String tablePrefix);

    void autoCheckComposeData();
}
