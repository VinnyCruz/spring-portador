package org.jazztech.portador.repository;

import java.util.List;
import java.util.UUID;
import org.jazztech.portador.repository.entity.CardHolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardHolderRepository extends JpaRepository<CardHolderEntity, UUID> {
    List<CardHolderEntity> findByStatus(CardHolderEntity.Status status);
}
