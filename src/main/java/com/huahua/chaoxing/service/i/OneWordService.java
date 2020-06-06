package com.huahua.chaoxing.service.i;

import java.util.ArrayList;

/**
 * @author LoveHuahua
 */
public interface OneWordService  {

    /**
     * 保存
     * @param word
     */
    void save(String word);

    /**
     * 获取随机语录
     * @param size 条数
     * @return
     */
    ArrayList<String> getWord(int size);
}
