package cnbi.zhaiwei.autocalibration.controller;

import cnbi.zhaiwei.autocalibration.service.AutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description
 *
 * @author cnbi 翟伟
 * @version 1.0
 * @taskId:
 * @createDate 2020/11/25 11:14
 * @project: autocalibration
 * @see cnbi.zhaiwei.autocalibration.controller
 */
@RestController
@RequestMapping(value = "/auto", method = RequestMethod.GET)
public class AutoController {
    @Autowired
    private AutoService service;

    @GetMapping(value = "/pad_compose")
    public int autoPaddingCompose(@RequestParam("table") String tableName, @RequestParam("update") String updatedTable) {
        return service.autoPaddingCompose(tableName, updatedTable);
    }

    @GetMapping(value = "/pad_subject")
    public int autoPaddingSubject(@RequestParam("table") String tableName) {
        //把前端给的表名统一转换成大写的
        return service.autoPaddingSubject(tableName.toUpperCase());
    }

    @GetMapping(value = "/check_compose")
    public int autoCheckComposeData(@RequestParam("table") String tableName, @RequestParam("update") String updatedTable){
        return service.autoUpdateComposeData(tableName, updatedTable);
    }

    /**
     * 把指定表的指定字段类型改成NVARCHAR2类型
     * @param table 指定的表
     * @return
     */
    @GetMapping(value = "/tran_field_type")
    public Integer transformFieldType(String table){
        return service.transformFieldType(table);
    }
}
