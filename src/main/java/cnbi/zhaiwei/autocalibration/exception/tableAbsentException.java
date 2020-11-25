package cnbi.zhaiwei.autocalibration.exception;

/**
 * Description
 *
 * @author cnbi 翟伟
 * @version 1.0
 * @taskId:
 * @createDate 2020/11/24 14:53
 * @project: autocalibration
 * @see cnbi.zhaiwei.autocalibration.exception
 */
public class tableAbsentException extends Throwable {
    public tableAbsentException(String table){
        System.out.println(table + "不存在，请检查后重新手动输入");
    }
}
