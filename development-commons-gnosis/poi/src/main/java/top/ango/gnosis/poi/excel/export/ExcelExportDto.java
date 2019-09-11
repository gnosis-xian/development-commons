package top.ango.gnosis.poi.excel.export;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 名称: ExcelExportDto
 * 描述:
 * </pre>
 *
 * @author yto.net.cn
 * @since 1.0.0
 */
public class ExcelExportDto implements Serializable {

    private String fileName;
    private Map maps;
    private List list;

    public ExcelExportDto() {

    }


    public ExcelExportDto(Map maps, List list, String fileName) {
        this.fileName = fileName;
        this.list = list;
        this.maps = maps;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Map getMaps() {
        return maps;
    }

    public void setMaps(Map maps) {
        this.maps = maps;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }
}
