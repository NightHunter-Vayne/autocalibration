package cnbi.zhaiwei.autocalibration.dao;

import cnbi.zhaiwei.autocalibration.pojo.Compose;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface AutomationMapper {
    /**
     * 向dw_compose_zw表中插入一条记录
     * @param compose 由一条要插入dw_compose_zw的记录封装成的对象
     * @author cnbi 翟伟
     * @date 2020/11/19
     */
    @Insert("insert into dw_compose_zw(SFACTFIELD,SDIMTABLE,SDIMFIELD,SNAME,CTYPE,SUBJECT,CISCOMPANY,NSORT)values(#{compose.factField}," +
    "#{compose.dimTable},#{compose.dimField},#{compose.name},#{compose.type},#{compose.subject},#{compose.cisCompany},#{compose.sort})")
    void insertDimensionField(@Param("compose") Compose compose);

    /**
     * 从一个指定表中查找出所有的字段名和对应的中文注释
     * @param tableName 指定的表
     * @return 所有的字段名和对应的中文注释组成的List集合
     * @author cnbi 翟伟
     * @date 2020/11/19
     */
    @Select("SELECT b.COLUMN_NAME,c.COMMENTS " +
            "FROM user_tables a " +
            "left join user_tab_columns b on a.TABLE_NAME=b.TABLE_NAME " +
            "left join user_col_comments c on a.TABLE_NAME=c.TABLE_NAME and b.COLUMN_NAME=c.COLUMN_NAME and  c.COMMENTS is not null " +
            "WHERE  a.TABLE_NAME = #{table}")
    List<Map> queryFactTableField(@Param("table") String tableName);

    /**
     * 查找出数据库中所有以指定前缀开头的表
     * @param tableName 前缀名+%
     * @return 符合需求的表的表名组成的集合
     * @author cnbi 翟伟
     * @date 2020/11/19
     */
    @Select("select distinct a.TABLE_NAME from user_tables a "+
            "left join user_tab_columns b on a.TABLE_NAME=b.TABLE_NAME "+
            "left join user_col_comments c on a.TABLE_NAME=c.TABLE_NAME and b.COLUMN_NAME=c.COLUMN_NAME " +
            "and c.COMMENTS is not null where a.TABLE_NAME like #{tablename}")
    List<String> queryFactTableName(@Param("tablename") String tableName);

    /**
     * 查找出指定表在subject表中对应的scode
     * @param tableName
     * @return
     * @author cnbi 翟伟
     * @date 2020/11/19
     */
    @Select("select scode from dw_subject where SFACTTABLE = #{table}")
    String querySubjectCode(@Param("table") String tableName);

    /**
     * 查找出在dw_compose_zw表中已经存储的事实、维度登字段，切这些字段来自一个事实表
     * @param subjectCode 指定的事实表的scode
     * @return 返回这些字段组成的集合
     * @author cnbi 翟伟
     * @date 2020/11/19
     */
    @Select("select SFACTFIELD from dw_compose_zw where SUBJECT = #{subject}")
    List<String> queryComposeTableData(@Param("subject") String subjectCode);

    /**
     *
     */
    @Update("")
    void updateComposeTableData();
}
