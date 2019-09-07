/*
 * @ClassName PermissionVO
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
public class PermissionVO extends BaseModel {
    /**
     * @Fields id ??ID
     */
    private Integer id;
    /**
     * @Fields url 
     */
    private String url;
    /**
     * @Fields name 
     */
    private String name;
    /**
     * @Fields memo 
     */
    private String memo;
    /**
     * @Fields createTime 
     */
    private Date createTime;
    /**
     * @Fields modifyTime 
     */
    private Date modifyTime;
}