package com.lidianwu.effective;

import java.util.regex.Pattern;

/**
 * Constants
 *
 * @author Created by ldianwu on 2019/9/21
 */
public interface Constants {

    // 可用的处理器个数
    int AVAILABLE_PROCESSOR = Runtime.getRuntime().availableProcessors();

    String OS_NAME = System.getProperty("os.name");

    String USER_HOME = System.getProperty("user.home");

    String LINE_SEPARATOR = System.getProperty("line.separator");

    String CHARSET = "UTF-8";

    /**
     * 数组分隔符
     */
    Pattern COMMA_SPLIT_PATTERN = Pattern.compile("\\s*[,;]+\\s*");
}
