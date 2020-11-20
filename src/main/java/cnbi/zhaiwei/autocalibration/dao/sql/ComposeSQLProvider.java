package cnbi.zhaiwei.autocalibration.dao.sql;

import cnbi.zhaiwei.autocalibration.pojo.Compose;
import org.apache.ibatis.jdbc.SQL;
import org.junit.Test;

import java.util.Objects;

public class ComposeSQLProvider {
    public String insert(Compose compose){
        SQL sql = new SQL();
        sql.INSERT_INTO("dw_compose_zw");

        if(Objects.nonNull(compose.getFactField())){
            sql.VALUES("SFACTFIELD", "#{factField}");
        }
        if(Objects.nonNull(compose.getDimTable())){
            sql.VALUES("SDIMTABLE", "#{dimTable}");
        }
        if(Objects.nonNull(compose.getDimField())){
            sql.VALUES("SDIMFIELD", "#{dimField}");
        }
        if(Objects.nonNull(compose.getName())){
            sql.VALUES("SNAME", "#{name}");
        }
        if(Objects.nonNull(compose.getType())){
            sql.VALUES("CTYPE", "#{type}");
        }
        if(Objects.nonNull(compose.getSubject())){
            sql.VALUES("SUBJECT", "#{subject}");
        }
        if(Objects.nonNull(compose.getCisCompany())){
            sql.VALUES("CISCOMPANY", "#{cisCompany}");
        }
        if(Objects.nonNull(compose.getSort())){
            sql.VALUES("NSORT", "#{sort}");
        }

        return sql.toString();
    }

    public String querySubjectCode(String table, String value, String fieldVal){
        SQL sql = new SQL();

        sql.FROM(table);
        sql.SELECT(value);
        if(Objects.nonNull(fieldVal)){
            sql.WHERE("SFACTTABLE = #{fieldVal}");
        }

        return sql.toString();
    }

    public String queryFactTableField(String table){
        SQL sql = new SQL();

        sql.FROM("user_tables a");
        sql.LEFT_OUTER_JOIN("user_tab_columns b");
        return sql.toString();
    }

    public String queryComposeTableData(String table, String value, String fieldVal){
        SQL sql = new SQL();

        sql.FROM(table);
        sql.SELECT(value);
        if(Objects.nonNull(fieldVal)){
            sql.WHERE("SUBJECT = #{fieldVal}");
        }

        return sql.toString();
    }


}
