package top.ango.gnosis.poi.excel.export;

/**
 * <pre>
 * 名称: ExcelExportSelectDto
 * 描述: 自定义excel导出自定义导出DTO
 * </pre>
 *
 * @author gnosis
 * @since 1.0.0
 */
public class ExcelExportSelectDto {
    /**
     * 字段英文名
     */
    private String key;
    /**
     * 字段中文名称
     */
    private String name;

    /**
     * 构造函数.
     */
    public ExcelExportSelectDto() {
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}