package eu.h2020.symbiote.batm.listeners.rest.controllers;

import eu.h2020.symbiote.batm.listeners.rest.interfaces.ICoupons;
import eu.h2020.symbiote.batm.models.Coupon;
import eu.h2020.symbiote.batm.services.CouponService;
import io.swagger.annotations.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Coupon controller
 *
 * @author jamsellem
 */

@RestController
public class CouponsController implements ICoupons {

    private static Log logger = LogFactory.getLog(CouponsController.class);

    @Autowired
    private CouponService couponService;


    @ApiOperation(value = "Request from another federated platform a coupon in exchange for your own coupon.")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Coupon could not be found!"),
            @ApiResponse(code = 400, message = "Invalid coupon!")})
    public ResponseEntity<Coupon> getCouponFromOtherPlatform(
            @RequestBody
            @ApiParam(value = "", required = true)
                    Coupon coupon) {

        // TODO
        // validate coupon
        // validate B&T deal
        // Generate new coupon
        // notify core about coupon creation

        // return new coupon

        return new ResponseEntity<Coupon>(new Coupon(), null, HttpStatus.OK);
    }


    @ApiOperation(value = "Request made by the client to obtain a coupon to access data from another federated platform. If available, the coupon will be returned immediately, otherwise, the process of generating and trading coupons will be triggered.")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Coupon could not be found!"),
            @ApiResponse(code = 400, message = "Invalid coupon!")})
    public ResponseEntity<Coupon> getCoupon(
            @PathVariable
            @ApiParam(value = "Request required federation identifier", required = true)
                String fedIdentifier,
            @RequestParam("resource")
            @ApiParam(value = "Request required resourceType", required = true)
                    String resourceType) {

        List<Coupon> list = couponService.findValidCouponByResourceTypeAndFedId(resourceType,fedIdentifier,1);
        if (list!= null && list.size() > 0){
            return new ResponseEntity<Coupon>(list.get(0), null, HttpStatus.OK);
        }else{
            // TODO
            // gen own coupon and request fedIdentifier for a coupon
            // new coupon = getCouponFromOtherPlatform()
            // return new coupon;
        }
        return new ResponseEntity<Coupon>(new Coupon(), null, HttpStatus.OK);
    }
}

