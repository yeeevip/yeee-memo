package vip.yeee.example.oracle;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

/**
 * description......
 *
 * @author yeeee
 * @since 2025/2/24 14:35
 */
public class CallProc {

    public String callProcDirectly(String input) {
        Connection conn = null;
        CallableStatement stmt = null;
        try {
//            conn = dataSource.getConnection();
//            stmt = conn.prepareCall("{call " + PROCEDURE_NAME + "(?, ?)}");

            // 设置输入参数
            stmt.setString(1, input);

            // 设置输出参数
            stmt.registerOutParameter(2, Types.VARCHAR);

            // 执行存储过程
            stmt.execute();

            // 获取输出参数
            String output = stmt.getString(2);
            return output;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        return "执行错误";
    }
}
