package org.example.inventory_manager_beta1.Repositories;

import org.example.inventory_manager_beta1.Entities.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupChatRepository extends JpaRepository<GroupChat, Long> {
    Optional<GroupChat> findByRoomName(String roomName);
    Optional<GroupChat> findByShipmentId(Integer shipmentId);
}
