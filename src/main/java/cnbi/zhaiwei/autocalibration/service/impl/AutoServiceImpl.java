package cnbi.zhaiwei.autocalibration.service.impl;

import cnbi.zhaiwei.autocalibration.dao.AutomationMapper;
import cnbi.zhaiwei.autocalibration.pojo.Compose;
import cnbi.zhaiwei.autocalibration.pojo.Subject;
import cnbi.zhaiwei.autocalibration.service.AutoService;
import cnbi.zhaiwei.autocalibration.util.DBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class AutoServiceImpl implements AutoService {
    private static final String CHINESE_NOTE = "COMMENTS";
    private static final String MAJOR_KEY = "nid";
    private static final String COLUMN_NAME = "SUBJECT";

    @Autowired
    private AutomationMapper mapper;

    @Override
    public int autoPaddingCompose(String tableName, String updatedTable) {
        //用于统计添加了多少条记录
        int count = 0;
        //获取指定表subject字段的值（subject表中获取指定表的scode值）
        String subjectCode = mapper.querySubjectCode(tableName.toLowerCase());
        //从指定表中获取所有的字段以及它们的中文注释
        List<Map> fieldList = mapper.queryFactTableField(tableName.toUpperCase());
        //把没有中文注释的字段去掉
        List<Map> filter = DBUtils.filterCollection(fieldList, CHINESE_NOTE);
        //判断是否有主题字段，如果没有则添加
        DBUtils.addifAbsentSubject(filter);

        //把字段名转换成小写
        List<String> dataList = DBUtils.toLowerCaseElement(
                //从updatedTable表(这里是DW_COMPOSE表)中获取 已经填充进去的值（指定表）
                mapper.queryComposeTableData(subjectCode, updatedTable)
        );

        for (Map<String, String> map : filter) {
            String colValue = map.get("COLUMN_NAME").toLowerCase();
            int divideIndex = colValue.indexOf("_") + 1;
            String prefix = colValue.substring(0, divideIndex);
            String factField = colValue.substring(divideIndex);

            if (Objects.equals(MAJOR_KEY, colValue) || dataList.contains(factField)) {
                continue;
            }

            Compose compose = new Compose();
            factField = DBUtils.discriminateFactField(prefix, factField, compose);

            compose.setFactField(factField);
            compose.setName(map.get(CHINESE_NOTE));
            compose.setSubject(subjectCode);
            compose.setCisCompany("N");

            count += mapper.insertDimensionField(compose, updatedTable);
        }

        return count;
    }

    @Override
    public void batchPaddingCompose(String tablePrefix) {
//        List<String> tableList = mapper.queryFactTableName(tablePrefix.toUpperCase() + "%");
//        for (String table : tableList) {
//            if(table.split("_").length > 2){
//                continue;
//            }
//            autoPaddingCompose(table);
//        }
    }

    @Override
    public List autoCheckComposeData(String tableName, String updatedTable) {
        //获取指定表subject字段的值（subject表中指定表的scode值）
        String subjectCode = mapper.querySubjectCode(tableName.toLowerCase());
        List<Map> list = mapper.queryFromComposeTable(subjectCode, updatedTable);
        //删除list中所有为null的元素
        list.removeAll(Collections.singleton(null));

        List<String> nids = new ArrayList();
        for (Map map : list) {
            String table = map.get("SDIMTABLE").toString();
            String name = mapper.queryTableExist(table);
            if (!StringUtils.hasText(name)) {
                nids.add(map.get("NID").toString());
            }
        }

        return nids;
    }

    /**
     * 向DW_SUBJECT主题表中添加新的数据（每条记录都是一个事实表的相关信息）
     *
     * @param tableName 要添加的事实表的表名
     */
    @Override
    public int autoPaddingSubject(String tableName) {
        //查询该事实表的中文注释
        String comments = mapper.queryTableComments(tableName);
        //查询该事实表的指定字段（此处是subject字段）的默认值
        String code = mapper.queryColumnDefault(tableName, COLUMN_NAME);

        Subject subject = new Subject();
        subject.setCode(code);
        subject.setName(comments);
        subject.setFactTable(tableName.toLowerCase());

        return mapper.insertRow2Subject(subject);
    }
}