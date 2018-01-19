package eu.h2020.symbiote.batm.repositories.impl;

import eu.h2020.symbiote.batm.models.Coupon;
import java.util.List;

/**
 * Coupon custom repository
 *
 * @author jamsellem
 */
public interface CouponRepositoryCustom {
    public List<Coupon> findValidCouponByResourceTypeAndFedId(String resourceType, String fedIdentifier, int limit);
}
