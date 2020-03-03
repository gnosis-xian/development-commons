package top.ango.gnosis.poi.excel.export;

import java.io.Serializable;

/**
 * <pre>
 * 名称: ExcelHeaderDto
 * 描述: 自定义excel导出列头
 * </pre>
 *
 * @author gnosis
 * @since 1.0.0
 */
public class ExcelHeaderDto implements Serializable {
    /**
     * 列头名称
     */
    private String headerName;
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 列宽
     */
    private Integer columnWidth;

    public ExcelHeaderDto() {
    }

    public ExcelHeaderDto(String headerName, String fieldName, Integer columnWidth) {
        this.headerName = headerName;
        this.fieldName = fieldName;
        this.columnWidth = columnWidth;
    }

    public String getHeaderName() {
        return this.headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Integer getColumnWidth() {
        return this.columnWidth;
    }

    public void setColumnWidth(Integer columnWidth) {
        this.columnWidth = columnWidth;
    }
}
