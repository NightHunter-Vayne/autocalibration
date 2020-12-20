package cnbi.zhaiwei.autocalibration.dao;

import cnbi.zhaiwei.autocalibration.pojo.Compose;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用于修正dw_compose表的数据
 *
 * @author cnbi 翟伟
 * @version 1.0
 * @taskId:
 * @createDate 2020/12/15 16:29
 * @project: autocalibration
 * @see cnbi.zhaiwei.autocalibration.dao
 */
@Mapper
public interface AutoUpdateMapper {
    /**
     * 根据指定的subject值在dw_compose_zw表中查出所有数据
     *
     * @param subjectCode
     * @return 返回dimtable组成的List集合
     * @author cnbi 翟伟
     * @date 2020/11/20
     */
    List<Map> queryFromComposeTable(@Param("subject") String subjectCode, @Param("update") String updatedTable);

    void updateComposeTable(@Param("update") String updateTable, @Param("fact") String factField, @Param("compose") Compose compose);
}
