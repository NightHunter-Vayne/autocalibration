package cnbi.zhaiwei.autocalibration.pojo;

import lombok.Data;

@Data
public class Compose {
    //事实表字段
    private String factField;
    //维度表
    private String dimTable;
    //维度主键字段
    private String dimField;
    //名称
    private String name;
    //类型(S主题D维度M度量)
    private String type;
    //主题编码
    private String subject;
    //已弃用
    private final String C_IS_COMPANY = "N";
    //排序
    private final int SORT = 0;
}
