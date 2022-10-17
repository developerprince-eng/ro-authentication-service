package com.retrospecsoptometrists.service.authentication.dtos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoleSummary implements Serializable {

    private static final long serialVersionUID = 6792364738741L;

    private String roleId;

    private String branchId;

    private String organisationalId;
}
