package eu.h2020.symbiote.batm.repositories;

import eu.h2020.symbiote.batm.models.Coupon;
import eu.h2020.symbiote.batm.repositories.impl.CouponRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Coupon  repository
 *
 * @author jamsellem
 */
public interface CouponRepository extends MongoRepository<Coupon, String>,CouponRepositoryCustom {
    public Coupon findById(String id);
}
