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
@RequestMapping(value= "/auto", method = RequestMethod.GET)
public class AutoController {
    @Autowired
    private AutoService service;

    @GetMapping(value = "/pad_compose")
    public int autoPaddingCompose(@RequestParam("table") String tableName){
        return service.autoPaddingCompose(tableName);
    }

    @GetMapping(value = "/pad_subject")
    public int autoPaddingSubject(@RequestParam("table") String tableName){
        return service.autoPaddingSubject(tableName);
    }

    @GetMapping(value = "/check_compose")
    public String autoCheckComposeData(@RequestParam("table") String tableName){
        List<String> nids = service.autoCheckComposeData(tableName);
        return nids.toString();
    }
}
