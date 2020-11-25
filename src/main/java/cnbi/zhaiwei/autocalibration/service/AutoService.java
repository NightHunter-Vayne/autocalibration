package cnbi.zhaiwei.autocalibration.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AutoService {
    /**
     * 自动将一个指定表中有汉语备注的维度字段
     * 及相关信息作为数据插入到dw_compose表中
     * @param tableName 指定表的表名称
     * @author cnbi 翟伟
     * @date 2020/11/19
     */
    int autoPaddingCompose(String tableName);

    /**
     * 批量将有特定前缀的一系列表中有汉语备注的维度
     * 字段及相关信息作为数据插入到dw_compose表中
     * @param tablePrefix 指定的表前缀
     * @author cnbi 翟伟
     * @date 2020/11/19
     */
    void batchPaddingCompose(String tablePrefix);

    /**
     *
     * @param tableName
     * @author cnbi 翟伟
     * @date 2020/11/20
     */
    List autoCheckComposeData(String tableName);

    /**
     * 向DW_SUBJECT表中添加新的数据
     * @param tableName
     */
    int autoPaddingSubject(String tableName);
}