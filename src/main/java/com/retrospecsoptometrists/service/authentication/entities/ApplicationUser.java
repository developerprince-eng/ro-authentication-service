package com.retrospecsoptometrists.service.authentication.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "application_user")
public class ApplicationUser implements Serializable {

    private static final long serialVersionUID = 6374812522148L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", columnDefinition = "BIGSERIAL")
    private Long userId;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "email_address", foreignKey = @ForeignKey(name = "email_address_fk"), referencedColumnName = "email_address")
    private UserProfile userProfile;

    private String encryptedPass;

    private String securityCode;

    public ApplicationUser(Long userId) {
        this.userId = userId;
    }
}
