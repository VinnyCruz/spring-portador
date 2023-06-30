package org.jazztech.portador.repository;

import java.util.List;
import java.util.UUID;
import org.jazztech.portador.repository.entity.CreditCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCardEntity, UUID> {
    List<CreditCardEntity> findAllByCardHolderId(UUID cardHolderId);

}
