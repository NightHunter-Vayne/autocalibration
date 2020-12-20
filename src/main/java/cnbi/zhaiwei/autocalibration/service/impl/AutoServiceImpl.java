package cnbi.zhaiwei.autocalibration.service.impl;

import cnbi.zhaiwei.autocalibration.dao.AutoUpdateMapper;
import cnbi.zhaiwei.autocalibration.dao.AutoInsertMapper;
import cnbi.zhaiwei.autocalibration.dao.FieldTypeMapper;
import cnbi.zhaiwei.autocalibration.dao.SubjectMapper;
import cnbi.zhaiwei.autocalibration.pojo.Compose;
import cnbi.zhaiwei.autocalibration.pojo.Subject;
import cnbi.zhaiwei.autocalibration.service.AutoService;
import cnbi.zhaiwei.autocalibration.util.DBUtils;
import cnbi.zhaiwei.autocalibration.util.FieldTransformUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class AutoServiceImpl implements AutoService {
    private static final String COLUMN_NAME = "COLUMN_NAME";
    private static final String CHINESE_NOTE = "COMMENTS";
    private static final String S_FACT_FIELD = "SFACTFIELD";
    private static final String MAJOR_KEY = "nid";
    private static final String SUBJECT = "SUBJECT";
    private static final String SEPARATION_CHARACTAR = "_";

    @Autowired
    private AutoInsertMapper insertMapper;
    @Autowired
    private AutoUpdateMapper updateMapper;
    @Autowired
    private SubjectMapper subjectMapper;
    @Autowired
    private FieldTypeMapper typeMapper;

    @Override
    public int autoPaddingCompose(String tableName, String updatedTable) {
        //用于统计添加了多少条记录
        int count = 0;
        //获取指定表subject字段的值（subject表中获取指定表的scode值）
        String subjectCode = insertMapper.querySubjectCode(tableName.toLowerCase());

        /*
         * 以下3行用于从原事实表中获取字段信息
         */
        //从指定表中获取所有的字段以及它们的中文注释
        List<Map> fieldList = insertMapper.queryFactTableField(tableName.toUpperCase());
        //把没有中文注释的字段去掉
        List<Map> filter = DBUtils.filterCollection(fieldList, CHINESE_NOTE);
        //判断是否有主题字段，如果没有则添加
        DBUtils.addifAbsentSubject(filter);


        //把字段名转换成小写
        List<String> dataList = DBUtils.toLowerCaseElement(
                //从updatedTable表(这里是DW_COMPOSE表)中获取 已经填充进去的值（指定表）
                insertMapper.queryComposeTableData(subjectCode, updatedTable)
        );

        for (Map<String, String> map : filter) {
            String colValue = map.get(COLUMN_NAME).toLowerCase();
            String simplyField = FieldTransformUtil.getSimpleField(colValue, SEPARATION_CHARACTAR);

            if (Objects.equals(MAJOR_KEY, colValue) || dataList.contains(simplyField)) {
                continue;
            }

            Compose compose = new Compose();
            FieldTransformUtil.paddingVarietyField(colValue, compose);
            FieldTransformUtil.paddingInvariantField(map.get(CHINESE_NOTE), subjectCode, compose);

            count += insertMapper.insertDimensionField(compose, updatedTable);
        }

        return count;
    }

    @Override
    public int autoUpdateComposeData(String tableName, String updatedTable) {
        //用于统计跟新了多少条记录
        int count = 0;
        /*
         * 以下3行用于从原事实表中获取字段信息
         */
        //从指定表中获取所有的字段以及它们的中文注释
        List<Map> fieldList = insertMapper.queryFactTableField(tableName.toUpperCase());
        //把没有中文注释的字段去掉
        List<Map> filter = DBUtils.filterCollection(fieldList, CHINESE_NOTE);
        //判断是否有主题字段，如果没有则添加
        DBUtils.addifAbsentSubject(filter);


        //获取指定表subject字段的值（subject表中指定表的scode值）
        String subjectCode = insertMapper.querySubjectCode(tableName.toLowerCase());
        //从dw_compose表中查询 SFACTFIELD,SDIMTABLE,SDIMFIELD,SNAME,CTYPE,SUBJECT 的值
        List<Map> list = updateMapper.queryFromComposeTable(subjectCode, updatedTable);
        //删除list中所有为null的元素
//        list.removeAll(Collections.singleton(null));

        Compose compose = new Compose();
        for(Map field : fieldList){
            String simpleField = "";
            String fieldName = field.get(COLUMN_NAME).toString();
            String comment = field.get(CHINESE_NOTE).toString();

            /**
             * 根据事实表中的字段名和汉语注释来填充Compose对象
             */
            FieldTransformUtil.paddingVarietyField(fieldName, compose);
            FieldTransformUtil.paddingInvariantField(comment, subjectCode, compose);

            /**
             * 查询dw_compose表原先SFACTFIELD字段的的值，因为它将作为update语句的条件
             */
            String factField = "";
            if(Objects.equals(fieldName.toUpperCase(), SUBJECT)){
                simpleField = fieldName;
            }else{
                simpleField = FieldTransformUtil.getSimpleField(fieldName, SEPARATION_CHARACTAR);
            }
            for(Map map : list){
                if(Objects.equals(map.get(S_FACT_FIELD).toString().toLowerCase(), simpleField.toLowerCase())){
                    factField = map.get(S_FACT_FIELD).toString();
                }
            }

            updateMapper.updateComposeTable(updatedTable, factField, compose);
            count++;
        }

        return count;
    }

    /**
     * 向DW_SUBJECT主题表中添加新的数据（每条记录都是一个事实表的相关信息）
     *
     * @param tableName 要添加的事实表的表名
     */
    @Override
    public int autoPaddingSubject(String tableName) {
        //查询该事实表的中文注释
        String comments = subjectMapper.queryTableComments(tableName);
        //查询该事实表的指定字段（此处是subject字段）的默认值
        String code = subjectMapper.queryColumnDefault(tableName, SUBJECT);

        Subject subject = new Subject();
        subject.setCode(code);
        subject.setName(comments);
        subject.setFactTable(tableName.toLowerCase());

        return subjectMapper.insertRow2Subject(subject);
    }

    @Override
    public Integer transformFieldType(String table) {
        Integer count = 0;
        List<Map> fields = typeMapper.gainFieldType(table);

        for(Map field : fields){
            Integer one = typeMapper.transformFieldType(table, field);
            count += one;
        }

        return count;
    }
}