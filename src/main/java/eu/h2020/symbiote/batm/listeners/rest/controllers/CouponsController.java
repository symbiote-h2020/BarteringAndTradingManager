package eu.h2020.symbiote.batm.listeners.rest.controllers;

import eu.h2020.symbiote.batm.dto.CouponDTO;
import eu.h2020.symbiote.batm.listeners.rest.interfaces.ICoupons;
import eu.h2020.symbiote.batm.models.Coupon;
import eu.h2020.symbiote.batm.services.CouponService;
import eu.h2020.symbiote.batm.utils.Rest;
import io.swagger.annotations.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Value("#{'${BTCore.url:localhost:8081}'}")
    private String coreUrl;

    @Autowired
    private CouponService couponService;


    @ApiOperation(value = "Request made by the client to obtain a coupon to access data from another federated platform. If available, the coupon will be returned immediately, otherwise, the process of generating and trading coupons will be triggered.")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Coupon could not be found!"),
            @ApiResponse(code = 400, message = "Invalid coupon!")})
    public ResponseEntity<CouponDTO> getCoupon(
            @PathVariable
            @ApiParam(value = "Request required federation identifier", required = true)
                String fedIdentifier,
            @RequestParam("resource")
            @ApiParam(value = "Request required resourceType", required = true)
                    String resourceType) {

        /**
         *
         *  has coupon for resource data ?
         *
         *
         *  if (true)
         *      return coupon
         *  else
         *      Generate new coupon
         *      notify core about coupon creation
         *      return new coupon
         *
         */

        CouponDTO dto = new CouponDTO();
        List<Coupon> list = couponService.findValidCouponByResourceTypeAndFedId(resourceType,fedIdentifier,1);
        if (list!= null && list.size() > 0){
            dto.coupon = list.get(0);
            dto.isNew = false;

        }else{
            // TODO
            // Generate new coupon
            dto.isNew = true;
            dto.coupon = new Coupon();


            // notify core about coupon creation
            Boolean use = new Rest<CouponDTO>().postReturningBooelanOrNull(this.coreUrl+"/create",null,null, dto.coupon );
        }
        return new ResponseEntity<CouponDTO>(dto, null, HttpStatus.OK);
    }
}

