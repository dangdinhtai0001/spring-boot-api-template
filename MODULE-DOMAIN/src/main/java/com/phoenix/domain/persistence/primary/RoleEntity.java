package com.phoenix.domain.persistence.primary;

import com.phoenix.domain.persistence.AuditEntity;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ROLE", schema = "template")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RoleEntity  {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "LEVEL", columnDefinition = "integer")
    private int level;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToMany(mappedBy = "roles")
    private List<UserEntity> users;

    @ManyToMany(mappedBy = "roles")
    private List<PermissionEntity> permissions;
}
