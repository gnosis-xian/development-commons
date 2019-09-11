package top.agno.gnosis.utils.mail;


import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gaojing [gaojing1996@vip.qq.com]
 */
@Slf4j
public class MailUtil {

    /**
     * @param mailString 待判断的mail邮件地址
     * @return boolean 是否匹配成功
     */
    public static boolean isMailAddr(String mailString) {
        String pattenString =
                "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        Pattern pattern = Pattern.compile(pattenString);
        Matcher matcher = pattern.matcher(mailString);
        return matcher.matches();
    }

    /**
     * @param num 验证码随机数的位数
     * @return String 随机数
     */
    public static String getVCode(int num) {
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer("");
        for (int i = 0; i < num; i++) {
            stringBuffer.append(random.nextInt(10));
        }
        return stringBuffer.toString();
    }

    public static boolean sendMailNotThrowException(MailProperties mailProperties, List<String> sendTo, String subject, String text) {
        MimeMail mimeMail = MimeMail.Builder.initMailSender(mailProperties.getHost(),
                mailProperties.getProtocol(),
                mailProperties.getPort(),
                mailProperties.getUsername(),
                mailProperties.getPassword(),
                mailProperties.getSmtpStarttlsEnable());
        try {
            log.info("准备给 {} 发送邮件 主题：{}; 内容：{}", sendTo, subject, text);
            mimeMail.sendMail(sendTo, subject, text);
        } catch (UnknownMailAddressException e) {
            log.error(e.getMessage());
            return false;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws UnknownMailAddressException {
        MimeMail mimeMail = MimeMail.Builder.initMailSender(
                "mail.yto.net.cn",
                "smtp",
                465,
                "yto_baoli@yto.net.cn",
                "YTObl2019",
                false);
        List<String> to = new ArrayList<>();// 收件人集合
        to.add("gaojing1996@vip.qq.com");
        to.add("815540606@qq.com");
        mimeMail.sendMail(to, "你有新的消息", "请到网站内查看" + new Date());
    }
}
