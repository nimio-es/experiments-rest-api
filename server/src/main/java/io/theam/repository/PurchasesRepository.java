package io.theam.repository;

import io.theam.model.Purchase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PurchasesRepository extends CrudRepository<Purchase, Long> {

    Collection<Purchase> findByCustomerId(long customerId);

    Collection<Purchase> findByProductId(long productId);

    Collection<Purchase> findByCustomerIdAndProductId(long customerId, long productId);

}
