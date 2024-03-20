package com.example.DACS.repository;

import com.example.DACS.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface IUserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u From User u WHERE u.username = ?1")
    User findByUsername(String username);

//    Modifying Được sử dụng để chỉ định rằng câu lệnh truy vấn là một câu lệnh cập nhật hoặc xóa dữ liệu.
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_role (user_id,role_id)" +
            "VALUES(?1, ?2)", nativeQuery = true)
    void addRoleToUser(Long userId, Long roleId);

    @Query("SELECT u.id FROM User u WHERE u.username = ?1")
    Long getUserIdByUsername(String username);

    @Query(value = "SELECT r.name FROM role r INNER JOIN user_role ur " +
           "ON r.id = ur.role_id WHERE ur.user_id = ?1", nativeQuery = true)
    String[] getRolesOfUser(Long userId);
}
