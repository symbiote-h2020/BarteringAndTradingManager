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

    public Coupon createCoupon(Coupon cupon){
        try{
            return couponRepo.save(cupon);
        }catch (javax.validation.ConstraintViolationException ex){
            return null;
        }

    }


    public Coupon findById(String id){
        return couponRepo.findById(id);
    }

    public Coupon update(Coupon coupon){
        return couponRepo.save(coupon);
    }

    public void delete(Coupon coupon){ couponRepo.delete(coupon); }


}
