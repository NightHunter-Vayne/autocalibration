package cnbi.zhaiwei.autocalibration.util;

import cnbi.zhaiwei.autocalibration.pojo.Compose;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Description
 *
 * @author cnbi 翟伟
 * @version 1.0
 * @taskId:
 * @createDate 2020/11/23 19:56
 * @project: autocalibration
 * @see cnbi.zhaiwei.autocalibration.util
 */
public class DBUtils {
    /**
     * 把一个List集合过滤
     *
     * @param originalList
     * @param key          返回所有有中文注释的
     * @return
     */
    public static List<Map> filterCollection(List<Map> originalList, String key) {
        List<Map> filter = new ArrayList();

        for (Map map : originalList) {
            if (Objects.nonNull(map.get(key))) {
                filter.add(map);
            }
        }

        return filter;
    }

    /**
     * 将一个List集合中所有字符串转换成小写再存入一个新的List集合中
     *
     * @param list 原存储字符串的List集合
     * @return
     */
    public static List<String> toLowerCaseElement(List<String> list) {
        List<String> newList = new ArrayList();
        for (String element : list) {
            String newEle = element.toLowerCase();
            newList.add(newEle);
        }
        return newList;
    }

    /**
     * 判断是否有主题字段
     *
     * @param mapList
     */
    public static void addifAbsentSubject(List<Map> mapList) {
        boolean exist = false;

        for (Map map : mapList) {
            String value = map.get("COLUMN_NAME").toString().toLowerCase();
            if (Objects.equals("subject", value) & Objects.nonNull(map.get("COMMENTS"))) {
                exist = true;
            }
        }

        if (!exist) {
            Map<String, String> map = new HashMap();
            map.put("COLUMN_NAME", "subject");
            map.put("COMMENTS", "主题");
        }
    }


    /**
     * 根据字段是维度、事实还是主题 来  分情况 给compose对象的属性填充值
     *
     * @param prefix    字段前缀
     * @param factField 字段除去前缀后的部分
     * @param compose   封装dw_compose表中一条记录的字段
     */
    public static String discriminateFactField(String prefix, String factField, Compose compose) {
        //对维度字段的处理
        if (Objects.equals(prefix, "dim_")) {
            compose.setDimTable("dw_dim" + factField);
            compose.setDimField("scode");
            compose.setType("D");
            //对度量（事实）字段的处理
        } else if (Objects.equals(prefix, "fact_")) {
            factField = factField.toUpperCase();
            compose.setDimTable("");
            compose.setDimField("");
            compose.setType("M");
            //没有前缀时 StringUtils.hasText()如果字符串里面的值为null， ""， "   "，那么返回值为false
        } else if (!StringUtils.hasText(prefix)) {
            //字段未subject时
            if (Objects.equals(factField, "subject")) {
                compose.setDimTable("dw_subject");
                compose.setDimField("scode");
                compose.setType("S");
            } else {
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
