package com.hipradeep.code.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id = 0;
    @Column(name = "user_name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 45)
    private String email;

    @Column(name = "password", nullable = false, length = 64)
    private String password;


    //@ManyToMany(mappedBy="followers")
    //@JoinTable(name="followingTable")


//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinTable(name = "followingTable",
//            joinColumns = {
//                    @JoinColumn(name = "user_id", referencedColumnName = "id")
//            },
//            inverseJoinColumns = {
//                    @JoinColumn(name = "following_id", referencedColumnName = "id")
//            }
//    )
//    private List<User> following;



//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinTable(name = "followerTable",
//            joinColumns = {
//                    @JoinColumn(name = "user_id", referencedColumnName = "id")
//            },
//            inverseJoinColumns = {
//                    @JoinColumn(name = "follower_id", referencedColumnName = "id")
//            }
//    )
//
//    private List<User> followers;

//    @ManyToMany(mappedBy="friendRequests")
//    @JoinTable(name="requestingTable")
//    private List<User> requesting;
//
//    @ManyToMany
//    @JoinTable(name="friendRequestTable")
//    private List<User> friendRequests;

//    @ManyToMany
//    private List<User> friends;

}

