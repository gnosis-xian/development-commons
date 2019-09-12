package top.agno.gnosis.utils.mail;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import top.agno.gnosis.utils.StringUtil;

import static top.agno.gnosis.constants.Constants.Mail.*;

/**
 * @Description:
 * @Author: gaojing [01381583@yto.net.cn]
 * @Date: Created in 18:38 2019/5/8
 * @Modified:
 */
@Data
@Configuration
@ConfigurationProperties(value = PREFIX)
public class MailProperties {

    private String host;

    private String protocol = "smtp";

    private int port = 465;

    private String username;

    private String password;

    private Boolean smtpStarttlsEnable = false;

}
