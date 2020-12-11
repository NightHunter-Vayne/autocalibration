package cnbi.zhaiwei.autocalibration.dao;

import cnbi.zhaiwei.autocalibration.pojo.Compose;
import cnbi.zhaiwei.autocalibration.pojo.Subject;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface AutomationMapper {
    /**
     * 向dw_compose_zw表中插入一条记录
     *
     * @param compose 由一条要插入dw_compose_zw的记录封装成的对象
     * @author cnbi 翟伟
     * @date 2020/11/19
     */
    int insertDimensionField(@Param("compose") Compose compose, @Param("update") String updatedTable);

    /**
     * 从一个指定表中查找出所有的字段名和对应的中文注释
     *
     * @param tableName 指定的表
     * @return 所有的字段名和对应的中文注释组成的List集合
     * @author cnbi 翟伟
     * @date 2020/11/19
     */
    List<Map> queryFactTableField(@Param("table") String tableName);

    /**
     * 查找出数据库中所有以指定前缀开头的表
     *
     * @param tableName 前缀名+%
     * @return 符合需求的表的表名组成的集合
     * @author cnbi 翟伟
     * @date 2020/11/19
     */
    List<String> queryFactTableName(@Param("tablename") String tableName);

    /**
     * 查找出指定表在subject表中对应的scode
     *
     * @param tableName
     * @return
     * @author cnbi 翟伟
     * @date 2020/11/19
     */
    String querySubjectCode(@Param("table") String tableName);

    /**
     * 查找出在dw_compose_zw表中已经存储的事实、维度等字段，且这些字段来自一个事实表
     *
     * @param subjectCode 指定的事实表的scode
     * @return 返回这些字段组成的集合
     * @author cnbi 翟伟
     * @date 2020/11/19
     */
    List<String> queryComposeTableData(@Param("subject") String subjectCode, @Param("update") String updatedTable);

    /**
     * 根据指定的subject值在dw_compose_zw表中查出所有数据
     *
     * @param subjectCode
     * @return 返回dimtable组成的List集合
     * @author cnbi 翟伟
     * @date 2020/11/20
     */
    List<Map> queryFromComposeTable(@Param("subject") String subjectCode, @Param("update") String updatedTable);

    /**
     * 判断一个表是否存在
     *
     * @param tableName 要查找的表名
     * @return 有返回值则存在，没有则说明不存在
     * @author cnbi 翟伟
     * @date 2020/11/20
     */
    String queryTableExist(@Param("table") String tableName);

    /**
     * 查询指定表的中文注释
     *
     * @param tableName
     * @return
     * @author cnbi 翟伟
     * @date 2020/11/24
     */
    String queryTableComments(@Param("table") String tableName);

    /**
     * 查询指定表的指定字段的默认值
     *
     * @param tableName
     * @param colName
     * @return
     * @author cnbi 翟伟
     * @date 2020/11/24
     */
    String queryColumnDefault(@Param("table") String tableName, @Param("column") String colName);

    /**
     * 向DW_SUBJECT表中添加新的记录，用Subject对象封装数据
     *
     * @param subject
     * @author cnbi 翟伟
     * @date 2020/11/24
     */
    int insertRow2Subject(@Param("subject") Subject subject);
}
