package com.lucky.apibackend.model.dto.interfaceinfo;

import com.lucky.apibackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author lucky
 * @date 2024/4/30
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InterfaceInfoSearchTextRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 3108921094751865617L;

    private String searchText;
}
