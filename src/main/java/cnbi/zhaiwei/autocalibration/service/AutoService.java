package cnbi.zhaiwei.autocalibration.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AutoService {
    /**
     * 自动将一个指定表中有汉语备注的维度字段
     * 及相关信息作为数据插入到dw_compose表中
     *
     * @param tableName 指定表的表名称
     * @author cnbi 翟伟
     * @date 2020/11/19
     */
    int autoPaddingCompose(String tableName, String updatedTable);

    /**
     * 批量将有特定前缀的一系列表中有汉语备注的维度
     * 字段及相关信息作为数据插入到dw_compose表中
     *
     * @param tablePrefix 指定的表前缀
     * @author cnbi 翟伟
     * @date 2020/11/19
     */
//    void batchPaddingCompose(String tablePrefix);

    /**
     * @param tableName
     * @author cnbi 翟伟
     * @date 2020/11/20
     */
    int autoUpdateComposeData(String tableName, String updatedTable);

    /**
     * 向DW_SUBJECT表中添加新的数据
     *
     * @param tableName
     */
    int autoPaddingSubject(String tableName);

    /**
     * 把指定表的指定字段类型改成NVARCHAR2类型
     * @param table 指定表
     * @return
     */
    Integer transformFieldType(String table);
}
