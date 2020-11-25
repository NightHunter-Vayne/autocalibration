package cnbi.zhaiwei.autocalibration.pojo;

import lombok.Data;

/**
 * 与数据库中的DW_Subject表相对应
 *
 * @author cnbi 翟伟
 * @version 1.0
 * @taskId:
 * @createDate 2020/11/23 21:00
 * @project: autocalibration
 * @see cnbi.zhaiwei.autocalibration.pojo
 */
@Data
public class Subject {
    private String code;
    private String name;
    private final String split = "_";
    private final String prefixDim = "dim";
    private final String prefixFact = "fact";
    private String factTable;
    private final Integer company = 1;
    private final Integer period = 2;
    private final String showType = "N";
}
