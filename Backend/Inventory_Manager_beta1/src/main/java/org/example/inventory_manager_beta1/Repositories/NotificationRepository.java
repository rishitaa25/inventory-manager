package org.example.inventory_manager_beta1.Repositories;

import jakarta.annotation.Nonnull;
import org.example.inventory_manager_beta1.DataEnums.AccessLevel;
import org.example.inventory_manager_beta1.DataEnums.Status;
import org.example.inventory_manager_beta1.Entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 *
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    /**
     *
     * @param id must not be {@literal null}.
     * @return
     */
    @Override
    @Nonnull
    Optional<Notification> findById (@Nonnull Integer id);

    List<Notification> findByRecipient (AccessLevel recipient);

    List<Notification> findByStatus(Status status);

    List<Notification> findByRecipientAndStatus(AccessLevel recipient, Status status);

    List<Notification> findByDate (LocalDate date);

    List<Notification> findByDriver(String driver);
}
