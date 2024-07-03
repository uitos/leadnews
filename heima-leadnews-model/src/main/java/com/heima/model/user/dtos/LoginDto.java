package com.heima.model.user.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录DTO
 *
 * @author admin
 * @name LoginDto
 * @date 2022-08-11 21:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户登录实体")
public class LoginDto {

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 密码，是未加密的
     */
    @ApiModelProperty("密码")
    private String password;

}