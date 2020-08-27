package com.l1yp.model;

import com.l1yp.enums.SexType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author Lyp
 * @Date 2020-08-27
 * @Email l1yp@qq.com
 */
@ApiModel("testParam")
public class TestParam {
    @ApiModelProperty(example = "1", value = "id")
    public Long id;
    @ApiModelProperty(example = "Lyp", value = "id")
    public String name;
    @ApiModelProperty(example = "MAN", value = "MAN")
    public SexType type;

    @Override
    public String toString() {
        return "TestParam{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
