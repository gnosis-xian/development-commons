package top.agno.gnosis.utils.mail;

/**
 * @author gaojing [gaojing1996@vip.qq.com]
 */
public class UnknownMailAddressException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * 非法的邮件地址
     */
    public UnknownMailAddressException() {
        // TODO Auto-generated constructor stub
        super("非法的邮件地址");
    }
}
