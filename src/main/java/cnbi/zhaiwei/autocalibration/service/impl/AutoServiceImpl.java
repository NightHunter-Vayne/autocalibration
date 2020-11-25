package cnbi.zhaiwei.autocalibration.service.impl;

import cnbi.zhaiwei.autocalibration.dao.AutomationMapper;
import cnbi.zhaiwei.autocalibration.exception.tableAbsentException;
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
    @Autowired
    private AutomationMapper mapper;

    @Override
    public int autoPaddingCompose(String tableName) {
        int count = 0;
        //获取指定表subject字段的值（subject表中指定表的scode值）
        String subjectCode = mapper.querySubjectCode(tableName.toLowerCase());
        //从DW_COMPOSE表中获取 已经填充进去的值（指定表）
        List<String> dataList = DBUtils.toLowerCaseElement(mapper.queryComposeTableData(subjectCode));
        //从指定表中获取所有的字段以及它们的中文注释
        List<Map> fieldList = mapper.queryFactTableField(tableName.toUpperCase());
        //把没有中文注释的字段去掉
        List<Map> filter = DBUtils.filterCollection(fieldList, "COMMENTS");
        //判断是否有主题字段，如果没有则添加
        DBUtils.addifAbsentSubject(filter);

        for (Map<String, String> map : filter) {
            String colValue = map.get("COLUMN_NAME").toLowerCase();
            int divideIndex = colValue.indexOf("_") + 1;
            String prefix = colValue.substring(0, divideIndex);
            String factField = colValue.substring(divideIndex);

            if(Objects.equals("nid",colValue)||dataList.contains(factField)){
                continue;
            }

            Compose compose = new Compose();
            factField = discriminateFactField(prefix, factField, compose);

            compose.setFactField(factField);
            compose.setName(map.get("COMMENTS"));
            compose.setSubject(subjectCode);
            compose.setCisCompany("N");

            count += mapper.insertDimensionField(compose);
        }

        return count;
    }

    @Override
    public void batchPaddingCompose(String tablePrefix) {
        List<String> tableList = mapper.queryFactTableName(tablePrefix.toUpperCase() + "%");
        for (String table : tableList) {
            if(table.split("_").length > 2){
                continue;
            }
            autoPaddingCompose(table);
        }
    }

    @Override
    public List autoCheckComposeData(String tableName) {
        //获取指定表subject字段的值（subject表中指定表的scode值）
        String subjectCode = mapper.querySubjectCode(tableName.toLowerCase());
        List<Map> list = mapper.queryFromComposeTable(subjectCode);
        //删除list中所有为null的元素
        list.removeAll(Collections.singleton(null));

        List<String> nids = new ArrayList();
        for(Map map : list){
            String table = map.get("SDIMTABLE").toString();
            String name = mapper.queryTableExist(table);
            if(!StringUtils.hasText(name)){
                nids.add(map.get("NID").toString());
            }
        }

        return nids;
    }

    @Override
    public int autoPaddingSubject(String tableName) {
        String comments = mapper.queryTableComments(tableName);
        String code = mapper.queryColumnDefault(tableName, "SUBJECT");

        Subject subject = new Subject();
        subject.setCode(code);
        subject.setName(comments);
        subject.setFactTable(tableName.toLowerCase());

        return mapper.insertRow2Subject(subject);
    }

    /**
     * 根据字段是维度、事实还是主题 来  分情况 给compose对象的属性填充值
     * @param prefix 字段前缀
     * @param factField 字段除去前缀后的部分
     * @param compose  封装dw_compose表中一条记录的字段
     */
    private String discriminateFactField(String prefix, String factField, Compose compose){
        //对维度字段的处理
        if(Objects.equals(prefix, "dim_")){
            compose.setDimTable("dw_dim" + factField);
            compose.setDimField("scode");
            compose.setType("D");
        //对度量（事实）字段的处理
        }else if(Objects.equals(prefix, "fact_")){
            factField = factField.toUpperCase();
            compose.setDimTable("");
            compose.setDimField("");
            compose.setType("M");
        //没有前缀时 StringUtils.hasText()如果字符串里面的值为null， ""， "   "，那么返回值为false
        }else if(!StringUtils.hasText(prefix)){
            //字段未subject时
            if(Objects.equals(factField, "subject")){
                compose.setDimTable("dw_subject");
                compose.setDimField("scode");
                compose.setType("S");
            }else{
                //这一部分是防止事实字段 没有以“fact_”开头
                compose.setDimTable("");
                compose.setDimField("");
                compose.setType("M");
            }
        }
        //返回处理后的factField
        return factField;
    }
}