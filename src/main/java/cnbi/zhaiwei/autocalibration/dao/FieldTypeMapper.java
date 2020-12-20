package cnbi.zhaiwei.autocalibration.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 该接口用于处理字段类型的转换
 *
 * @author cnbi 翟伟
 * @version 1.0
 * @taskId:
 * @createDate 2020/12/18 17:17
 * @project: autocalibration
 * @see cnbi.zhaiwei.autocalibration.dao
 */
@Mapper
public interface FieldTypeMapper {
    /**
     * 获取指定表的字段名，对应字段类型，字段长度
     * @param table 表名
     * @return
     */
    List<Map> gainFieldType(@Param("table") String table);

    /**
     * 把指定表的指定字段类型改成NVARCHAR2类型
     * @param table
     * @param map
     * @return
     */
    Integer transformFieldType(@Param("table") String table, Map map);
}
