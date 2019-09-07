/*
 * @ClassName RoleVO
 * @Description 
 * @version 1.0
 * @Date 2019-09-06 09:28:42
 */
package com.geeku.model.innovative;

import com.geeku.model.BaseModel;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* Created by Mybatis Generator on 2019/09/06
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleVO extends BaseModel {
    /**
     * @Fields id 主键
     */
    private Integer id;
    /**
     * @Fields name 姓名
     */
    private String name;
    /**
     * @Fields memo 备注
     */
    private String memo;
    /**
     * @Fields createTime 创建时间
     */
    private Date createTime;
    /**
     * @Fields time 
     */
    private Date time;
}