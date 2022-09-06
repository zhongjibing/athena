package com.icezhg.athena.vo;

import com.icezhg.athena.domain.Menu;
import lombok.Data;

import java.util.List;

/**
 * Created by zhongjibing on 2022/09/07.
 */
@Data
public class MenuTree {

    private Integer id;
    private String label;
    private Menu menu;
    private List<MenuTree> children;
}
