package com.phoenix.domain.persistence.primary;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "USER", schema = "template")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @Column(name ="LOCKED")
    private boolean locked;

    @Column(name ="ENABLED")
    private boolean enabled;


    /**
     *  * name: Tên của joinTable (USER_ROLE),
     *  * joinColumns: Tên column trong bảng joinTable mà bảng USER sẽ foreign key tới,
     *  * inverseJoinColumns: Tên column trong bảng joinTable mà bảng ROLE sẽ foreign key tới.
     */
    @ManyToMany
    @JoinTable(
            name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")
    )
    private List<RoleEntity> roles;

}

/**
 * Chúng ta sẽ đặt annotation @ManyToMany cùng với khai báo một biến Collection trong cả hai entity Developer
 * và Project và để thể hiện mối quan hệ nhiều nhiều, chúng ta sẽ sử dụng một annotation khác tên là
 * @JoinTable trong một trong hai entity này. Các bạn có thể đặt annotation @JoinTable ở đâu cũng được,
 * nếu annotation này nằm trong entity @Developer thì trong entity Project các bạn phải khai báo thêm thuộc
 * tính mappedBy trong annotation @ManyToMany và ngược lại.
 *
 */
