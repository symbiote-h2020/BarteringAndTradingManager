package eu.h2020.symbiote.batm.config;

import com.google.gson.Gson;
import eu.h2020.symbiote.batm.BarteringAndTradingManager;
import eu.h2020.symbiote.batm.models.Coupon;
import eu.h2020.symbiote.batm.services.CouponService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;


//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {BarteringAndTradingManager.class,AppConfig.class})
public class CouponServiceTest {

    @Autowired
    private CouponService couponService;

//    @Test
    public void saveCouponTest() {
        Coupon newCoupon = couponService.save(createNewCoupon());

        assertThat(newCoupon).isNotNull();

        couponService.delete(newCoupon);
    }

//    @Test
    public void saveConstraintExceptionTest() {
        Coupon coupon = new Coupon();

        assertThat(coupon.getUsedByList()).isEqualTo(new ArrayList<>());

        Coupon newCoupon = couponService.save(coupon);

        assertThat(newCoupon).isNull();
    }

//    @Test
    public void updateCouponTest() {
        String resouceType ="double_test";
        Coupon coupon = couponService.save(createNewCoupon());

        Coupon couponToUpdate = new Coupon(coupon);
        assertThat(compareCoupon(coupon,couponToUpdate)).isTrue();

        couponToUpdate.setResourceType(resouceType);

        Coupon newCoupon = couponService.update(couponToUpdate);

        assertThat(newCoupon).isNotNull();
        assertThat(compareCoupon(coupon,couponToUpdate)).isFalse();
        assertThat(newCoupon.getResourceType()).isEqualTo(resouceType);

        couponService.delete(newCoupon);
    }

//    @Test
    public void findValidCouponByResourceTypeAndFedIdTest(){
        Coupon newCoupon = couponService.save(createNewCoupon());

        List<Coupon> foundCouponList = couponService.findValidCouponByResourceTypeAndFedId(newCoupon.getResourceType(), newCoupon.getFedIdentifier(),  1);

        assertThat(foundCouponList).isNotNull();

        couponService.delete(newCoupon);
    }

//    @Test
    public void findByIdTest() {
        Coupon newCoupon = couponService.save(createNewCoupon());

        Coupon foundCoupon = couponService.findById(newCoupon.getId());

        assertThat(compareCoupon(newCoupon,foundCoupon)).isTrue();

        couponService.delete(newCoupon);
    }


    /*******************
     *  AUX FUNCTIONS  *
     *******************/

    private Coupon createNewCoupon(){
        Date d = new Date();

        Coupon c = new Coupon();

        c.setIssuer("123456789_TESTE");
        c.setBeneficiary("test");
        c.setFedIdentifier("test");
        c.setResourceType("test");
        c.setExpirationDate(d);
        c.setSingleUse(false);
        c.addToUsedByList("teste");

        return c;
    }

    private boolean compareCoupon(Coupon a, Coupon b) {

        if (a.getId().equalsIgnoreCase(b.getId()) &&
            a.getIssuer().equalsIgnoreCase(b.getIssuer()) &&
            a.getBeneficiary().equalsIgnoreCase(b.getBeneficiary()) &&
            a.getFedIdentifier().equalsIgnoreCase(b.getFedIdentifier()) &&
            a.getResourceType().equalsIgnoreCase(b.getResourceType()) &&
            a.getExpirationDate().getTime() == b.getExpirationDate().getTime() &&
            a.isSingleUse() == b.isSingleUse() &&
            new Gson().toJson(a.getUsedByList()).equalsIgnoreCase(new Gson().toJson(a.getUsedByList()))) {

            return true;
        }
        return false;
    }

}
