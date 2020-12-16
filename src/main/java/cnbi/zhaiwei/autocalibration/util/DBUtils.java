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
}
