package cnbi.zhaiwei.autocalibration.dao;

import cnbi.zhaiwei.autocalibration.pojo.Subject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用于操作主题表
 * @author cnbi 翟伟
 * @version 1.0
 * @taskId:
 * @createDate 2020/12/17 9:02
 * @project: autocalibration
 * @see cnbi.zhaiwei.autocalibration.dao
 */
@Mapper
public interface SubjectMapper {
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
