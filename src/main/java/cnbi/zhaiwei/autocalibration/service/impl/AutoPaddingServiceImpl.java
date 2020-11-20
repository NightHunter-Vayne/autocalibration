package cnbi.zhaiwei.autocalibration.service.impl;

import cnbi.zhaiwei.autocalibration.dao.AutomationMapper;
import cnbi.zhaiwei.autocalibration.pojo.Compose;
import cnbi.zhaiwei.autocalibration.service.AutoPaddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class AutoPaddingServiceImpl implements AutoPaddingService {
    @Autowired
    private AutomationMapper mapper;

    @Override
    public void autoPaddingCompose(String tableName) {
        String subjectCode = mapper.querySubjectCode(tableName.toLowerCase());
        List<String> dataList = toLowerCaseElement(mapper.queryComposeTableData(subjectCode));
        List<Map> fieldList = mapper.queryFactTableField(tableName);

        for (Map<String, String> map : fieldList) {
            String colValue = map.get("COLUMN_NAME").toLowerCase();
            int start = colValue.indexOf("_") + 1;
            String factField = colValue.substring(start);

            if(Objects.isNull(map.get("COMMENTS"))||Objects.equals("nid",colValue)||dataList.contains(factField)){
                continue;
            }

            Compose compose = new Compose();
            discriminateFactField(colValue, compose);

            compose.setFactField(factField);
            compose.setName(map.get("COMMENTS"));
            compose.setSubject(subjectCode);
            compose.setCisCompany("N");

            mapper.insertDimensionField(compose);
        }
    }

    @Override
    public void batchPaddingCompose(String tablePrefix) {
        List<String> tableList = mapper.queryFactTableName(tablePrefix.toUpperCase() + "%");
        for (String table : tableList) {
            autoPaddingCompose(table);
        }
    }

    @Override
    public void autoCheckComposeData() {

    }

    /**
     * 根据字段是维度、事实还是主题 来  分情况 给compose对象的属性填充值
     * @param colValue 表中的字段名称
     * @param compose  封装dw_compose表中一条记录的字段
     */
    private void discriminateFactField(String colValue, Compose compose){
        if(colValue.startsWith("dim_")){
            compose.setDimTable(colValue);
            compose.setDimField("scode");
            compose.setType("D");
        }else if(colValue.startsWith("fact_")){
            compose.setDimTable("");
            compose.setDimField("");
            compose.setType("M");
        }else{
            compose.setDimTable("dw_" + colValue);
            compose.setDimField("scode");
            compose.setType("S");
        }
    }

    /**
     * 将一个List集合中所有字符串转换成小写再存入一个新的List集合中
     * @param list 原存储字符串的List集合
     * @return
     */
    private List<String> toLowerCaseElement(List<String> list){
        List<String> newList = new ArrayList();
        for (String element : list) {
            String newEle = element.toLowerCase();
            newList.add(newEle);
        }
        return newList;
    }
}
