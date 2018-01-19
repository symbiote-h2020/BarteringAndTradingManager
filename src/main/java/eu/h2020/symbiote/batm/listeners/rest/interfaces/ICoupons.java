package eu.h2020.symbiote.batm.listeners.rest.interfaces;

import eu.h2020.symbiote.batm.models.Coupon;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Coupon controller interface
 *
 * @author jamsellem
 */
@RequestMapping("/coupon")
public interface ICoupons {

    @PostMapping
    ResponseEntity<Coupon> getCouponFromOtherPlatform(@RequestBody Coupon coupon);

    @GetMapping("/{fedIdentifier}")
    ResponseEntity<Coupon> getCoupon(@PathVariable String fedIdentifier, @RequestParam("resource") String resourceType);

}
