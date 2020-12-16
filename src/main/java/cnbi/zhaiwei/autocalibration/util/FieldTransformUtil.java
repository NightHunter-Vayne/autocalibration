package cnbi.zhaiwei.autocalibration.util;

import cnbi.zhaiwei.autocalibration.pojo.Compose;

import java.util.Objects;

/**
 * Description
 *
 * @author cnbi 翟伟
 * @version 1.0
 * @taskId:
 * @createDate 2020/12/15 21:48
 * @project: autocalibration
 * @see cnbi.zhaiwei.autocalibration.util
 */
public class FieldTransformUtil {
    private static final String FACT_ID = "fact";
    private static final String DIMENSION_ID = "dim";
    private static final String SUBJECT_ID = "s";
    private static final String SEPARATION_CHARACTAR = "_";
    private static final String DIMENSION_PREFIX = "dw_dim";
    private static final String SUBJECT_PREFIX = "dw_";
    private static final String DIMFIELD = "scode";
    private static final String FACT_TYPE = "M";
    private static final String DIMENSION_TYPE = "D";
    private static final String SUBJECT_TYPE = "S";

    /**
     * 填充那些随着字段类型（事实、维度、主题）变化的属性
     * @param field
     * @return
     */
    public static void paddingVarietyField(String field, Compose compose){
        Integer index = field.indexOf(SEPARATION_CHARACTAR);
        String prefix = "";
        if(field.contains(SEPARATION_CHARACTAR)){
            prefix = field.substring(0, index);
        }

        if(Objects.equals(prefix.toLowerCase(), FACT_ID)){
            String simpleFact = getSimpleField(field, SEPARATION_CHARACTAR);

            compose.setFactField(simpleFact.toUpperCase());
            compose.setDimTable("");
            compose.setDimField("");
            compose.setType(FACT_TYPE);
        }else if(Objects.equals(prefix.toLowerCase(), DIMENSION_ID)){
            String simpleDimension = getSimpleField(field, SEPARATION_CHARACTAR);
            String dimensionTableName = dimensionField2DimensionTable(simpleDimension, DIMENSION_PREFIX);

            compose.setFactField(simpleDimension.toLowerCase());
            compose.setDimTable(dimensionTableName.toLowerCase());
            compose.setDimField(DIMFIELD);
            compose.setType(DIMENSION_TYPE);
        }else if(Objects.equals(field.substring(0, 1).toLowerCase(), SUBJECT_ID)){
            String subjectTablename = getSubjectTablename(field, SUBJECT_PREFIX);

            compose.setFactField(field.toLowerCase());
            compose.setDimTable(subjectTablename.toLowerCase());
            compose.setDimField(DIMFIELD);
            compose.setType(SUBJECT_TYPE);
        }
    }

    /**
     * 填充那些随着字段类型（事实、维度、主题）不变的属性
     * @param comments 汉语注释
     * @param code subject编码
     * @param compose
     */
    public static void paddingInvariantField(String comments, String code,Compose compose){
        compose.setName(comments);
        compose.setSubject(code);
    }

    /**
     * 根据事实表中的维度字段或事实字段获取dw_compose表中的维度字段或事实字段
     * @param field 事实表中的维度字段
     * @return
     */
    public static String getSimpleField(String field, String separation){
        Integer lineIndex = field.indexOf(separation);
        return field.substring(lineIndex+1);
    }

    /**
     * 根据维度字段名获得维度表的表名
     * @param simpleDimension 维度字段名
     * @param prefix 维度表的前缀
     * @return
     */
    public static String dimensionField2DimensionTable(String simpleDimension, String prefix){
        return prefix + simpleDimension;
    }

    /**
     * 获取主题表的表名
     * @param subject 表示主题字段的字段名（就是"subject"）
     * @param prefix 主题表的前缀
     * @return
     */
    public static String getSubjectTablename(String subject, String prefix){
        return prefix + subject;
    }
}
