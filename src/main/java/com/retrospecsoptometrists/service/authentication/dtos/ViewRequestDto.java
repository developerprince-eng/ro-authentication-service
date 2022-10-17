package com.retrospecsoptometrists.service.authentication.dtos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ViewRequestDto implements Serializable {

    private static final long serialVersionUID = 8467235473467L;

    private Long organisationalId;

    private String branchId;

    private String emailAddress;
}
