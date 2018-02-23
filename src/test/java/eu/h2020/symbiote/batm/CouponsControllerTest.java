package eu.h2020.symbiote.batm;


import com.google.gson.Gson;
import eu.h2020.symbiote.batm.dto.CouponDTO;
import eu.h2020.symbiote.batm.listeners.rest.controllers.CouponsController;
import eu.h2020.symbiote.batm.models.Coupon;
import eu.h2020.symbiote.batm.services.CouponService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest()
@TestPropertySource(locations = "classpath:test.properties")
@Configuration
public class CouponsControllerTest {

    @Autowired
    private CouponService couponService;



    @Test
    public void contextLoads() {
        CouponsController cc = new CouponsController();

        assertThat(cc).isNotNull();
    }


//    @Test
//    public void getCouponTest() throws Exception {
//        Date d = new Date();
//
//        Coupon c = new Coupon();
//
//        c.setIssuer("123456789_TESTE");
//        c.setBeneficiary("test");
//        c.setFedIdentifier("test");
//        c.setResourceType("test");
//        c.setExpirationDate(d);
//        c.setSingleUse(false);
//
//        Coupon newCoupon = couponService.createCoupon(c);
//        throw new Exception("new coupon created " + new Gson().toJson(newCoupon));
////        String id = newCoupon.getId();
////
////
////
////        ResponseEntity<CouponDTO> response  = new CouponsController().getCoupon("test","test");
////
////        CouponDTO dto = response.getBody();
////        assertThat(dto.isNew).isFalse();
////
////        couponService.delete(c);
//
//    }

}
