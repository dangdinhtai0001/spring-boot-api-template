package com.phoenix.domain.persistence.primary;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "PERMISSION", schema = "template")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PermissionEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @ManyToMany
    @JoinTable(
            name ="ROLE_PERMISSION",
            joinColumns = @JoinColumn(name ="PERMISSION_ID"),
            inverseJoinColumns = @JoinColumn(name ="ROLE_ID")
    )
    private List<RoleEntity> roles;
}
