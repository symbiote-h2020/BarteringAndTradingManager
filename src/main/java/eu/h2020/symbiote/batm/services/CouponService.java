package eu.h2020.symbiote.batm.services;

import eu.h2020.symbiote.batm.models.Coupon;
import eu.h2020.symbiote.batm.repositories.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Coupon service
 *
 * @author jamsellem
 */
@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepo;

    public List<Coupon> findValidCouponByResourceTypeAndFedId(String resourceType, String fedIdentifier,  int limit){
        return couponRepo.findValidCouponByResourceTypeAndFedId(resourceType,fedIdentifier,limit);
    }

}
