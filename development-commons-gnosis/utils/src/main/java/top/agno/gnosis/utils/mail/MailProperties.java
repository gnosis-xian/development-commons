package top.agno.gnosis.utils.mail;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
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
public class MailProperties {

    @Autowired
    private Environment environment;

    private String host = this.environment.getProperty(HOST);

    private String protocol = StringUtil.isEmpty(this.environment.getProperty(PROTOCOL)) ? "smtp" : this.environment.getProperty(PROTOCOL);

    private int port = StringUtil.isEmpty(this.environment.getProperty(PORT)) ? 465 : Integer.parseInt(this.environment.getProperty(PORT));

    private String username = this.environment.getProperty(USERNAME);

    private String password = this.environment.getProperty(PASSWORD);

    private boolean smtpStarttlsEnable = StringUtil.isEmpty(this.environment.getProperty(SMTP_START_TLS_ENABLE)) ? false : Boolean.parseBoolean(this.environment.getProperty(SMTP_START_TLS_ENABLE));
}
