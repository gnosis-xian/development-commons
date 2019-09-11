package top.ango.gnosis.poi.excel.imports;

import lombok.Data;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;

/**
 * 文件基本参数定义
 * @author : zhangbolong
 * @since : 2019-8-21
 */
@Data
public class ExcelImportParams implements Serializable{

	private static final long serialVersionUID = -4231868339831975335L;
	
	private String filePath;
	
	private String classPath;
	
	private Integer rowNumIndex;
	
	private Integer sheetIndex;

	private InputStream inputStream;
	
	private Map map;



	

}
