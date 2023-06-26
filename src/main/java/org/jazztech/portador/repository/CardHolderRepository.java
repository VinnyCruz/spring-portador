package org.jazztech.portador.repository;

import java.util.UUID;
import org.jazztech.portador.repository.entity.CardHolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardHolderRepository extends JpaRepository<CardHolderEntity, UUID> {
}
